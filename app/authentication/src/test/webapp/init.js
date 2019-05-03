var deps = [];
var TEST_REGEXP = /Spec\.js$/i;
for (var file in window.__karma__.files) {

    var pathToModule = function (path) {
        return path.replace(/^\/base\/app\//, '').replace(/\.js$/, '');
    };

    Object.keys(window.__karma__.files).forEach(function (file) {
        if (TEST_REGEXP.test(file)) {
            // Normalize paths to RequireJS module names.
            deps.push(file);
        }
    });
}

var baseUrl = '/base/src/main/webapp/';