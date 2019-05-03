/**
 * Created by ggradnig on 2015-04-28
 */

//Load the base configuration
var baseConfig = require('./karma.conf.js');

module.exports = function(config) {
    // Load base config
    baseConfig(config);

    // Override base config
    config.set({

        // Switch to a headless PhantomJS browser for tests
        browsers : [ 'PhantomJS' ],

        // Only run once in CI mode
        singleRun : true,

        // Do no watch for file changes
        autoWatch : false
    });
};