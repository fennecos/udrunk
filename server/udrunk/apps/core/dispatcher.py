apps    = ['place',]
db_name = 'geo'

class DatabaseRouter(object):
    def db_for_read(self, model, **hints):
        "Point all operations on place models to 'geo'"
        if model._meta.app_label in apps:
            return db_name
        return None

    def db_for_write(self, model, **hints):
        "Point all operations on place models to 'geo'"
        if model._meta.app_label in apps:
            return db_name
        return None

    def allow_relation(self, obj1, obj2, **hints):
        "Allow any relation if a model in place is involved"
        if obj1._meta.app_label in apps or obj2._meta.app_label in apps:
            return True
        return None

    def allow_syncdb(self, db, model):
        "Make sure the place place only appears on the 'geo' db"
        if db == db_name:
            return model._meta.app_label in apps
        elif model._meta.app_label in apps:
            return False
        return None
