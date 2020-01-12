package io.github.msh91.arch.data.model.response

class DomainErrorException(val errorModel: ErrorModel): Throwable() {
}