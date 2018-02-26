var AndroidHass = {

    /* If true, the user is an administrator, false otherwise */
    _admin: false,

    /* Because I don't know when all the items I'll hide are available, I'll be checking it within an interval */
    _setAdminTimeout: null,

    /* Fires when Android event onBackPressed is fired. Returns true if event is handled. False otherwise. */
    onBackPressed: function(){
        // If a dialog is visible -> Close it
        if (this.isMoreInfoDialogVisible()){
            history.back();
            return true;
        }

        // If "Overview" screen is not displayed, display it
        if (location.href.indexOf("/states") == -1){
            this.getDrawerItem("states").click();
            return true;
        }

        return false;
    },

    /* Hides an item from drawer menu */
    showDrawerItem: function(name, show){
        this.getDrawerItem(name).style.display = show ? "" : "none";
    },

    /* Returns an item from drawer menu */
    getDrawerItem: function(name){
        return document.querySelector("home-assistant").shadowRoot.querySelector("home-assistant-main").shadowRoot.querySelector("#drawer").querySelector("ha-sidebar").shadowRoot.querySelector("paper-listbox > paper-icon-item[data-panel='" + name + "']");
    },

    /* Hides developer tools from drawer menu */
    showDeveloperTools: function(show){
        document.querySelector("home-assistant").shadowRoot.querySelector("home-assistant-main").shadowRoot.querySelector("#drawer").querySelector("ha-sidebar").shadowRoot.querySelector("div.dev-tools").parentNode.style.display = show ? "" : "none";
    },

    /* Returns true if a "more info" dialog is visible. False otherwise */
    isMoreInfoDialogVisible: function(){
        return document.querySelector("home-assistant").shadowRoot.querySelector("home-assistant-main").shadowRoot.querySelector("more-info-dialog").shadowRoot.querySelector("paper-dialog").style.display != "none";
    },

    /* Returns true if the user is an administrator, false otherwise */
    isAdmin: function(){
        return _admin;
    },

    /* Sets the user as admin, or not, and sets drawer menu items visibility */
    setAdmin: function(value){
        if (this._setAdminTimeout != null){
            clearTimeout(this._setAdminTimeout);
        }

        try{
            AndroidHass.showDrawerItem("logbook", value);
            AndroidHass.showDrawerItem("config", value);
            AndroidHass.showDrawerItem("logout", value);
            AndroidHass.showDeveloperTools(value);
            this._admin = value;
        }catch(err){
            // Elements are not available yet, we will try again after some time
            var that = this;
            this._setAdminTimeout = setTimeout(function(){
                that.setAdmin(value);
            }, 200);
        }
    }

};