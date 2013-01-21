###
# SIGNALS
###
from django.conf import settings
from django.utils.translation import ugettext_noop as _
from django.contrib.auth.models import User
from django.db.models import signals
from tastypie.models import create_api_key
from apps.core.models import Friend

signals.post_save.connect(create_api_key, sender=User)

def send_friend_request(sender, instance, **kwargs):
	from notification import models as notification
	notification.send([instance.user2], "friends_invite", {"from_user": instance.user1})
signals.post_save.connect(send_friend_request, sender=Friend)

###
# DATE FUNCTIONS
###
import time, datetime

def prettydate(d):
    diff = datetime.datetime.utcnow() - d
    s = diff.seconds
    if diff.days > 7 or diff.days < 0:
        return d.strftime('%d %b %y')
    elif diff.days == 1:
        return '1 day ago'
    elif diff.days > 1:
        return '%s days ago' % diff.days
    elif s <= 1:
        return 'just now'
    elif s < 60:
        return '%s seconds ago' % s
    elif s < 120:
        return '1 minute ago'
    elif s < 3600:
        return '%s minutes ago' % s/60
    elif s < 7200:
        return '1 hour ago'
    else:
        return '%s hours ago' % s/3600

def epoch(value):
    try:
        return int(time.mktime(value.timetuple())*1000)
    except AttributeError:
        return ''