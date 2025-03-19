package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Entity
import java.util.UUID

// Whatever. It works. lmao
// And yes, I don't even use a map to *at least* have decent retrieval from IDs. rofl
abstract class AbstractRepository<T : Entity>(protected val database: MutableList<T>) {
	fun findAll(): List<T> {
		return database
	}

	fun findById(id: UUID): T? {
		return database.find { it.id == id }
	}

	fun findBy(predicate: (T) -> Boolean): T? {
		return database.find(predicate)
	}

	fun filterBy(predicate: (T) -> Boolean): List<T> {
		return database.filter(predicate)
	}

	fun insert(entity: T): T {
		database.add(entity)
		return entity
	}

	fun update(entity: T): T {
		val removed = database.removeIf { it.id == entity.id }
		if (!removed) throw NoSuchElementException()
		database.add(entity)
		return entity
	}

	fun delete(entity: T): T {
		val removed = database.removeIf { it.id == entity.id }
		if (!removed) throw NoSuchElementException()
		return entity
	}
}
