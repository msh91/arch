---
name: write-tests
description: >
  MUST be used whenever writing, editing, or running unit tests in this project.
  Trigger this skill when creating test files, updating test cases, or running test commands.
  Contains mandatory testing conventions, patterns, and run commands for the Arcyto Android project.
allowed-tools: Read, Edit, Write, Bash, Grep, Glob
---

You MUST follow every guideline below when writing, editing, or running tests in this project.

---

## 1. Test Location, Dependencies & Naming

- **Unit tests:** `src/test/kotlin/` — mirrors the source package structure.
- **Test dependency:** Always use `testImplementation(projects.core.tooling.test)` — it re-exports JUnit4, MockK, Truth,
  Turbine, and Coroutines Test. Never add these individually.
- **Test class:** `*Test` suffix matching the class under test. One test class per source class.
- **Test methods:** Backtick-quoted descriptive sentences, e.g.
  `` `invoke should return sorted prices when repository succeeds` ``.

---

## 2. Running Tests

```bash
./gradlew test                         # All unit tests
./gradlew :feature:history:impl:test   # Single module
./gradlew build                        # Full build + tests
```

After writing or modifying tests, always run the relevant module's tests to verify they pass.

---

## 3. Test Pattern

This pattern applies to all test classes (ViewModel, UseCase, Repository, Mapper, etc.). Adapt as needed — ViewModel
tests require `MainDispatcherRule`; simpler classes don't.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MyFeatureViewModelTest {
    // Only needed in ViewModel tests
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mock all dependencies
    private val myUseCase: MyUseCase = mockk()
    private val errorMapper: CompositeErrorMapper = mockk()

    // System under test — initialized lazily so @Before stubs apply first
    private val sut by lazy { createViewModel() }

    @Before
    fun setup() {
        // Common stubs shared across tests
        every { errorMapper.map(any()) } returns "Error"
    }

    @Test
    fun `verify initial state is Loading`() = runTest {
        setupSuccessResponse()

        assertThat(sut.uiState.value).isEqualTo(MyUiState.Loading)
    }

    @Test
    fun `verify state transitions to Success when data loads`() = runTest {
        setupSuccessResponse()

        sut.uiState.test {
            if (awaitItem() is MyUiState.Loading) {
                val success = awaitItem() as MyUiState.Success
                assertThat(success.items).hasSize(2)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `verify error emits snackbar event`() = runTest {
        coEvery { myUseCase.invoke(any()) } returns Result.failure(IOException("fail"))

        sut.events.test {
            assertThat(awaitItem()).isEqualTo(MyUiEvent.ShowSnackbar("Error"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel() = MyFeatureViewModel(
        myUseCase = myUseCase,
        errorMapper = errorMapper,
    )

    private fun setupSuccessResponse() {
        coEvery { myUseCase.invoke(any()) } returns Result.success(testData)
    }
}
```

**Key rules:**

- Initialize `sut` with `by lazy` — stubs in `@Before` run first, then `sut` is created on first access.
- Use `MainDispatcherRule` in ViewModel tests only.
- Use `runTest { }` for all coroutine tests.
- One assertion concept per test.
- Create helper methods: `createViewModel()`, `setupSuccessResponse()`, etc.

---

## 4. MockK

- `mockk<MyType>()` — strict by default, prefer over relaxed.
- `coEvery` / `coVerify` for suspend functions; `every` / `verify` for regular.
- `any()` for params you don't care about; exact values for params that matter.

---

## 5. Assertions (Google Truth)

ALWAYS use Google Truth — never JUnit `assertEquals` or Kotlin `assert`.

```kotlin
assertThat(actual).isEqualTo(expected)
assertThat(result.isSuccess).isTrue()
assertThat(list).hasSize(3)
assertThat(list).containsExactly(a, b, c).inOrder()
assertThat(state).isInstanceOf(MyUiState.Success::class.java)
```

---

## 6. Flow Testing (Turbine)

```kotlin
sut.uiState.test {
    assertThat(awaitItem()).isEqualTo(MyUiState.Loading)
    val success = awaitItem() as MyUiState.Success
    assertThat(success.items).hasSize(2)
    cancelAndIgnoreRemainingEvents()
}
```

- ALWAYS call `cancelAndIgnoreRemainingEvents()` at the end of `test {}` blocks.
- Use `awaitItem()` — never `expectMostRecentItem()` unless testing hot flows.
