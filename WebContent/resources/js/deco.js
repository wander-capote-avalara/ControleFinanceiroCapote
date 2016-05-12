(function(angular) {
    angular.module("DecoApp", ["ngAnimate", "ngTouch", "ui.bootstrap", "DecoModels"])
        .service("servicodeSoma", function($http) {
            this.somar = function(a, b) {
                var s = (a / b).toFixed(2);
                return isNaN(s) ? 0 : s;
            };

            this.enviaProservidor = function(soma) {
                return $http.get("/salva", {
                    params: {
                        valor: soma
                    }
                }).then(function(r) {

                })
            }
        })
        .factory("factorydeSoma", function() {
            return {
                somar: function(a, b) {
                    var s = (a / b).toFixed(2);
                    return isNaN(s) ? 0 : s;
                }
            }
        })

    .controller("DecoController", DecoController).controller("otoControler", function($scope, Person) {
        // body...
    });

    function DecoController($scope, servicodeSoma, factorydeSoma, Person) {
        this.saudacao="oi";
        
        $scope.pessoa = {
            nome: "Wander",
            idade: 19
        }

        var j = new Person("wander");

        j.setaNome("ivan");

        $scope.names = [{id:1, name:"a"}, {id:2, name:"b"}, {id:3, name:"c"}];

        $scope.somar = servicodeSoma.somar;

        $scope.enviaProSeicor = function(argument) {
            //servicodeSoma.enviaProSeicor(servicodeSoma.somar($scope.valor1, $scope.valor2));
        }
    }
})(angular);