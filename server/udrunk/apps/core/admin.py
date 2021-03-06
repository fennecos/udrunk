# -*- coding: utf-8 -*-

from django.contrib import admin
from apps.core import models as cm
import settings

media = settings.MEDIA_URL

class MultiDBModelAdmin(admin.ModelAdmin):
    # A handy constant for the name of the alternate database.
    using = 'default'

    def save_model(self, request, obj, form, change):
        # Tell Django to save objects to the 'other' database.
        obj.save(using=self.using)

    def delete_model(self, request, obj):
        # Tell Django to delete objects from the 'other' database
        obj.delete(using=self.using)

    def queryset(self, request):
        # Tell Django to look for objects on the 'other' database.
        return super(MultiDBModelAdmin, self).queryset(request).using(self.using)

    def formfield_for_foreignkey(self, db_field, request=None, **kwargs):
        # Tell Django to populate ForeignKey widgets using a query
        # on the 'other' database.
        return super(MultiDBModelAdmin, self).formfield_for_foreignkey(db_field, request=request, using=self.using, **kwargs)

    def formfield_for_manytomany(self, db_field, request=None, **kwargs):
        # Tell Django to populate ManyToMany widgets using a query
        # on the 'other' database.
        return super(MultiDBModelAdmin, self).formfield_for_manytomany(db_field, request=request, using=self.using, **kwargs)


class FriendAdmin(MultiDBModelAdmin):
	model = cm.Friend
	
class CheckinAdmin(MultiDBModelAdmin):
	model = cm.Checkin
	
class ProfileAdmin(MultiDBModelAdmin):
	model = cm.Profile	
	
admin.site.register(cm.Friend, FriendAdmin)
admin.site.register(cm.Checkin, CheckinAdmin)
admin.site.register(cm.Profile, ProfileAdmin)

