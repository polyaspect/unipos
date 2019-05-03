/**
 * Created by ggradnig on 2015-04-28
 */

module.exports = function (config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '../../../',

        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine', 'requirejs', 'es6-shim'],

        // list of files / patterns to load in the browser
        files: [
            'src/test/webapp/init.js',
            'src/main/webapp/main.js',
            'src/main/webapp/assets/js/jquery/2.1.4/jquery.min.js',
            {pattern: 'src/main/webapp/**/!(*spec*).*', included: false},
            {pattern: 'src/test/webapp/**/!(*spec*).*', included: false}
        ],

        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
            'src/main/webapp/main.js': ['babel'],
            'src/main/webapp/app.js': ['babel'],
            'src/main/webapp/components/**/*.js': ['babel'],
        },
        babelPreprocessor: {
            options: {
                presets: [['es2015', {"strict": false}]],
                sourceMap: 'inline',
                compact: false
            }
        },
        plugins: [
            'karma-jasmine',
            'karma-es6-shim',
            'karma-phantomjs-launcher',
            'karma-chrome-launcher',
            'karma-requirejs',
            'karma-babel-preprocessor',
            'babel-preset-es2015'
        ],

        // test results reporter to use
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['progress'],

        // web server port
        port: 9876,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['Chrome'],

        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false
    });
};