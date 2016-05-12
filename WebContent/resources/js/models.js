(function(angular){
    angular.module("DecoModels", [])
    .factory("Person", function($http) {
            function Person(nome) {
                this.nome = nome;
            }

            Person.prototype.setaNome = function(novoNome) {
                this.nome = novoNome;
            }
            Person.prototype.save = function() {
                // return $http.post("/salvapesso", this).then(function (r) {
                //     // body...
                // }).catch(function (e) {
                //     // body...
                // });
            }

            return Person;
        })
})(angular)