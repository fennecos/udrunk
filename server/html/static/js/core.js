/***
 * VIEWS
 ***/
window.HomeView = Backbone.View.extend({
    template:_.template($('#home').html()),
    render:function (eventName) {
		var that = this;
		$(this.el).html(this.template());	
		$(this.collection).each(function(k, item) {
			$('#checkins', that.el).append(new CheckinItemView({model: item}).render().el);
		});
        return this;
    }
});

window.CheckinView = Backbone.View.extend({
    template:_.template($('#checkinpage').html()),
    render:function (eventName) {
		var that = this;
		$(this.el).html(this.template({item: this.model.toJSON()}));	
        return this;
    }
});

/* Fragments */
window.CheckinItemView = Backbone.View.extend({
    render:function (eventName) {
		var template = _.template( $("#checkinitem").html(), {item: this.model.toJSON()});
        $(this.el).html(template);
        return this;
    }
});


/***
 * CONTROLLERS
 ***/
var AppRouter = Backbone.Router.extend({

    routes:{
        "":"home",
        "checkins/:id":"checkinDetails"
    },

    initialize:function () {
        // Handle back button throughout the application
        $('.back').live('click', function(event) {
            window.history.back();
            return false;
        });
        this.firstPage = true;
    },

    home:function () {
		var that = this;
		this.feed = new Feed();
		this.feed.fetch({success: function() { 
			that.changePage(new HomeView({collection: that.feed.models}));
		}});
        
    },

    checkinDetails:function (id) {
		var that = this;
		this.checkin = new Checkin({id: id});
		this.checkin.fetch({success: function() {
			that.changePage(new CheckinView({model: that.checkin}));
		}});        
    },

   
    changePage:function (page) {
        $(page.el).attr('data-role', 'page');
        page.render();
        $('body').append($(page.el));
        var transition = $.mobile.defaultPageTransition;
        // We don't want to slide the first page
        if (this.firstPage) {
            transition = 'none';
            this.firstPage = false;
        }
        $.mobile.changePage($(page.el), {changeHash:false, transition: transition});
    }

});

$(document).ready(function () {
    app = new AppRouter();
    Backbone.history.start();
});