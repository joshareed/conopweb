package conopweb

import com.mongodb.*

class MongoService {
	def grailsApplication
	private def _mongo
	boolean transactional = false

	MongoService() {
		// some convenience methods
		DBCollection.metaClass {
			count << { LinkedHashMap query -> delegate.count(query as BasicDBObject) }
			list << {  -> delegate.find() }
			findAll << { LinkedHashMap query -> delegate.find(query as BasicDBObject) }
			findAll << { LinkedHashMap query, LinkedHashMap filter -> delegate.find(query as BasicDBObject, filter as BasicDBObject) }
			find << { LinkedHashMap query -> delegate.findOne(query as BasicDBObject) }
			find << { LinkedHashMap query, LinkedHashMap filter -> delegate.findOne(query as BasicDBObject, filter as BasicDBObject) }
			distinct << { String key, LinkedHashMap query -> delegate.distinct(key, query as BasicDBObject) }
			add << { LinkedHashMap doc ->
				def dbo = doc as BasicDBObject
				delegate.insert(dbo)
				dbo
			}
			update << { LinkedHashMap q, BasicDBObject op -> delegate.update(q as BasicDBObject, op) }
			remove << { LinkedHashMap query -> delegate.remove(query as BasicDBObject) }
		}

		DBCursor.metaClass {
			sort << { LinkedHashMap doc -> delegate.sort(doc as BasicDBObject) }
		}
	}

	MongoService(host, db) {
		this()
		grailsApplication = [config: [mongo: [host: host, db: db]]]
	}

	def propertyMissing(name) {
		def collection = getCollection(name)
		if (collection) {
			MongoService.metaClass."$name" = collection
		}
		return collection
	}

	def getMongo() {
		if (!_mongo) {
			// create our Mongo instance if needed
			def host = grailsApplication?.config?.mongo?.host
			def servers = host.split(',').collect { new ServerAddress(it) }
			_mongo = new Mongo(servers)
		}
		_mongo
	}

	def getCollection(name, create = false) {
		if (!name) { return null }

		// get our database and collection
		def db = mongo.getDB(grailsApplication?.config?.mongo?.db)
		if (db.collectionExists(name)) {
			return db.getCollection(name)
		} else {
			if (create) {
				return db.createCollection(name, [:] as BasicDBObject)
			} else {
				return null
			}
		}
	}
}
