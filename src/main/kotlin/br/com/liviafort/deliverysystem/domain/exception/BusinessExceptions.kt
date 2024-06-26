package br.com.liviafort.deliverysystem.domain.exception

class EntityAlreadyExistsException(message: String?) : RuntimeException(message)

class EntityNotFoundException(message: String?) : RuntimeException(message)
