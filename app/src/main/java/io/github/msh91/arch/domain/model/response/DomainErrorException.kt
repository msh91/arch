package io.github.msh91.arch.domain.model.response

class DomainErrorException(val errorModel: ErrorModel): Throwable() {
}