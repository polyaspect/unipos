//define(['angularMocks'], function () {
//    describe('Unipos-Data:', function () {
//        var $controller,
//            $httpBackend,
//            $scope,
//            $rootScope,
//            $q,
//            ctrl,
//            service,
//            responseFindOneProduct = {
//                id: "55c662cf44ae6d56cf0887f0",
//                number: 4,
//                name: "Joyce alter Laptop",
//                description: "Acer Aspire 7745G",
//                price: 999.99,
//                category: {
//                    id: "55c662cf44ae6d56cf0887ec",
//                    name: "Technik",
//                    taxRate: {
//                        id: "55c662cf44ae6d56cf0887e8",
//                        name: "Entertainment",
//                        description: "Cinema",
//                        percentage: 10,
//                        taxRateCategory: "NORMAL"
//                    }
//                }
//            },
//            responseFindAllProducts = [{
//                id: "55c662cf44ae6d56cf0887ed",
//                number: 1,
//                name: "Red Bull",
//                description: "Red Bull Energy Drink 355ml",
//                price: 1.59,
//                category: {
//                    id: "55c662cf44ae6d56cf0887ea",
//                    name: "GetrÃ¤nke",
//                    taxRate: {
//                        id: "55c662cf44ae6d56cf0887e8",
//                        name: "Entertainment",
//                        description: "Cinema",
//                        percentage: 10,
//                        taxRateCategory: "NORMAL"
//                    }
//                }
//            }, {
//                id: "55c662cf44ae6d56cf0887ee",
//                number: 2,
//                name: "LG G3",
//                description: "LG D855",
//                price: 356,
//                category: {
//                    id: "55c662cf44ae6d56cf0887ec",
//                    name: "Technik",
//                    taxRate: {
//                        id: "55c662cf44ae6d56cf0887e8",
//                        name: "Entertainment",
//                        description: "Cinema",
//                        percentage: 10,
//                        taxRateCategory: "NORMAL"
//                    }
//                }
//            }],
//            responseFindAllTaxRates = [{
//                id: "55c662cf44ae6d56cf0887e8",
//                name: "Entertainment",
//                description: "Cinema",
//                percentage: 10,
//                taxRateCategory: "NORMAL"
//            }, {
//                id: "55c662cf44ae6d56cf0887e9",
//                name: "Food",
//                description: "Bevarages",
//                percentage: 20,
//                taxRateCategory: "NORMAL"
//            }],
//            responseFindAllCategories = [{
//                id: "55c662cf44ae6d56cf0887ea",
//                name: "GetrÃ¤nke",
//                taxRate: {
//                    id: "55c662cf44ae6d56cf0887e8",
//                    name: "Entertainment",
//                    description: "Cinema",
//                    percentage: 10,
//                    taxRateCategory: "NORMAL"
//                }
//            }, {
//                id: "55c662cf44ae6d56cf0887eb",
//                name: "Cordon",
//                taxRate: {
//                    id: "55c662cf44ae6d56cf0887e9",
//                    name: "Food",
//                    description: "Bevarages",
//                    percentage: 20,
//                    taxRateCategory: "NORMAL"
//                }
//            }, {
//                id: "55c662cf44ae6d56cf0887ec",
//                name: "Technik",
//                taxRate: {
//                    id: "55c662cf44ae6d56cf0887e8",
//                    name: "Entertainment",
//                    description: "Cinema",
//                    percentage: 10,
//                    taxRateCategory: "NORMAL"
//                }
//            }],
//            responseNextProductNumber = {"product": {"number":1}},
//            responseStoreGuids = ["1", "2", "3"],
//            responseCompanies = [
//                {
//                    "id": "56785af9e4b082948920f0e5",
//                    "name": "Unipos IT Solutions e.U.",
//                    "uid": "123456789",
//                    "commercialRegisterNumber": "AT123456789",
//                    "stores": [
//                        {
//                            "id": "56793fb6e4b07a77efa51afb",
//                            "storeId": 6,
//                            "name": "Haupt",
//                            "address": {
//                                "street": "Wien",
//                                "postCode": 1130,
//                                "city": "Wien",
//                                "country": "Ã–sterreich"
//                            },
//                            "companyGuid": "unipos",
//                            "guid": "f61477f5-ec58-4313-9002-63781c89b817",
//                            "closeHour": null,
//                            "controllerStore": false
//                        },
//                        {
//                            "id": "568069dbe4b0a20c25e971b7",
//                            "storeId": 7,
//                            "name": "Neben",
//                            "address": {
//                                "street": "Wien",
//                                "postCode": 1130,
//                                "city": "Wien",
//                                "country": "Ã–sterreich"
//                            },
//                            "companyGuid": "unipos",
//                            "guid": "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                            "closeHour": null,
//                            "controllerStore": false
//                        },
//                        {
//                            "id": "56806ab6e4b0a20c25e971b8",
//                            "storeId": 8,
//                            "name": "Verkaufsstand 10",
//                            "address": {
//                                "street": "Wien",
//                                "postCode": 1100,
//                                "city": "Wien",
//                                "country": "Ã–sterreich"
//                            },
//                            "companyGuid": "unipos",
//                            "guid": "8276144a-e4fc-4f9e-9b4d-ca720960151a",
//                            "closeHour": null,
//                            "controllerStore": false
//                        }
//                    ],
//                    "guid": "unipos"
//                },
//                {
//                    "id": "56cecbb653e665dbe34e5e61",
//                    "name": "Test2",
//                    "uid": "00000000",
//                    "commercialRegisterNumber": "00000000",
//                    "stores": [
//                        {
//                            "id": "56cecbca53e665dbe34e5e62",
//                            "storeId": 9,
//                            "name": "TestFiliale",
//                            "address": {
//                                "street": "Test",
//                                "postCode": 1100,
//                                "city": "Wien",
//                                "country": "Ã–sterreich"
//                            },
//                            "companyGuid": "281ddae2-e22e-4045-9e3c-f3e956e0389f",
//                            "guid": "31286284-3c93-4427-a0a2-15a577d5b18d",
//                            "closeHour": "1970-01-01T20:00:00",
//                            "controllerStore": true
//                        }
//                    ],
//                    "guid": "281ddae2-e22e-4045-9e3c-f3e956e0389f"
//                }
//            ],
//            responseAdminProductsCompanyGuid = [
//                {
//                    "id": "56c760f653e6a1a996735b2d",
//                    "number": 11,
//                    "productIdentifier": 17,
//                    "name": "Monitor",
//                    "description": null,
//                    "price": null,
//                    "customPriceInputAllowed": true,
//                    "category": {
//                        "id": "563ba941e4b043b6fd583a29",
//                        "name": "Technik",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": null
//                    },
//                    "stores": [
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a",
//                        "31286284-3c93-4427-a0a2-15a577d5b18d"
//                    ],
//                    "guid": "4e17f339-41e3-4428-a8c9-b08f84729263"
//                },
//                {
//                    "id": "56806c03e4b0a20c25e971bf",
//                    "number": 3,
//                    "productIdentifier": 7,
//                    "name": "Fallout: New Vegas",
//                    "description": null,
//                    "price": 19.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "56806aece4b0a20c25e971b9",
//                        "name": "Games",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": "54ca7091-5392-4118-a05b-b45cde1c56f3"
//                    },
//                    "stores": [
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a"
//                    ],
//                    "guid": "3b61a6ac-07f1-422a-8f81-0d3ac5504f13"
//                },
//                {
//                    "id": "56810170e4b0a20c25e971c8",
//                    "number": 7,
//                    "productIdentifier": 12,
//                    "name": "Batman: Arkham Origins",
//                    "description": null,
//                    "price": 19.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "56806aece4b0a20c25e971b9",
//                        "name": "Games",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": "54ca7091-5392-4118-a05b-b45cde1c56f3"
//                    },
//                    "stores": [
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a",
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d"
//                    ],
//                    "guid": "89933d67-f747-4520-95af-7e0d36933ad8"
//                },
//                {
//                    "id": "56806afae4b0a20c25e971bb",
//                    "number": 1,
//                    "productIdentifier": 5,
//                    "name": "Fallout 4",
//                    "description": null,
//                    "price": 59.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "56806aece4b0a20c25e971b9",
//                        "name": "Games",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": "54ca7091-5392-4118-a05b-b45cde1c56f3"
//                    },
//                    "stores": [
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a",
//                        "31286284-3c93-4427-a0a2-15a577d5b18d"
//                    ],
//                    "guid": "4c33536c-8381-4c91-b6bf-b67e4b436a68"
//                },
//                {
//                    "id": "56806c03e4b0a20c25e971be",
//                    "number": 2,
//                    "productIdentifier": 6,
//                    "name": "Fallout 3",
//                    "description": null,
//                    "price": 19.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "56806aece4b0a20c25e971b9",
//                        "name": "Games",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": "54ca7091-5392-4118-a05b-b45cde1c56f3"
//                    },
//                    "stores": [
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a",
//                        "31286284-3c93-4427-a0a2-15a577d5b18d"
//                    ],
//                    "guid": "976c7cc7-c0bc-4179-ae47-0bc4ad7bb88a"
//                },
//                {
//                    "id": "56810170e4b0a20c25e971c7",
//                    "number": 6,
//                    "productIdentifier": 11,
//                    "name": "Batman: Arkham City",
//                    "description": null,
//                    "price": 19.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "56806aece4b0a20c25e971b9",
//                        "name": "Games",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": "54ca7091-5392-4118-a05b-b45cde1c56f3"
//                    },
//                    "stores": [
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a"
//                    ],
//                    "guid": "fa83c43e-5f70-4cd6-99b3-b08d8b12f52f"
//                },
//                {
//                    "id": "56810170e4b0a20c25e971c6",
//                    "number": 5,
//                    "productIdentifier": 10,
//                    "name": "Batman: Arkham Asylum",
//                    "description": null,
//                    "price": 9.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "56806aece4b0a20c25e971b9",
//                        "name": "Games",
//                        "taxRate": {
//                            "id": "568443dce4b0c1f7fd611b53",
//                            "name": "Ermäßigter Steuersatz",
//                            "description": "Essen, Bücher, Tickets, etc.",
//                            "percentage": 10,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "9efb3c03-fe70-4c87-9c64-da71a76d384c"
//                        },
//                        "guid": "54ca7091-5392-4118-a05b-b45cde1c56f3"
//                    },
//                    "stores": [
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a"
//                    ],
//                    "guid": "70e30a23-4852-4c31-937e-70c2b145822c"
//                },
//                {
//                    "id": "56af4fcf78ecbd8d4fdf9faf",
//                    "number": 9,
//                    "productIdentifier": 15,
//                    "name": "Sprite",
//                    "description": null,
//                    "price": 1.99,
//                    "customPriceInputAllowed": false,
//                    "category": {
//                        "id": "563ba941e4b043b6fd583a27",
//                        "name": "Getränke",
//                        "taxRate": {
//                            "id": "568443e3e4b0c1f7fd611b54",
//                            "name": "Normalsteuersatz",
//                            "description": "Waren + Dienstleistungen",
//                            "percentage": 20,
//                            "taxRateCategory": "NORMAL",
//                            "guid": "31c55b67-fc9e-4765-8c40-af0472b01ec0"
//                        },
//                        "guid": null
//                    },
//                    "stores": [
//                        "f61477f5-ec58-4313-9002-63781c89b817",
//                        "974b0f41-1a95-4e3e-95f4-7cd02f294e0d",
//                        "8276144a-e4fc-4f9e-9b4d-ca720960151a"
//                    ],
//                    "guid": "f53ff83e-870c-4ff0-848f-a5e77c221f12"
//                }
//            ];
//
//
//        beforeEach(module('unipos.data'));
//
//        beforeEach(inject(function (_$controller_, _$httpBackend_, _$rootScope_, _$q_, productService) {
//            $controller = _$controller_;
//            $httpBackend = _$httpBackend_;
//            $rootScope = _$rootScope_;
//            $scope = _$rootScope_.$new();
//            $q = _$q_;
//            ctrl = $controller('ProductController', {$rootScope: $rootScope, $scope: $scope});
//            service = productService;
//
//            $httpBackend.when('GET', '/data/adminProducts').respond(responseFindAllProducts);
//            $httpBackend.when('POST', '/data/products').respond();
//            $httpBackend.when('GET', '/data/stores/guid').respond();
//            $httpBackend.when('GET', '/data/products/productNumberOrName').respond(responseFindAllProducts);
//            $httpBackend.when('GET', '/data/adminProducts/highestProductNumberProduct').respond(responseNextProductNumber);
//            $httpBackend.when('DELETE', '/data/products/dbId').respond({});
//            $httpBackend.when('GET', '/data/taxRates').respond(responseFindAllTaxRates);
//            $httpBackend.when('GET', '/data/categories').respond(responseFindAllCategories);
//            $httpBackend.when('GET', '/data/companies').respond(responseCompanies);
//            $httpBackend.when('GET', '/data/adminProducts/companyGuid').respond(responseAdminProductsCompanyGuid);
//
//
//            $httpBackend.flush();
//        }));
//
//        describe('Product Service', function() {
//            it('should response the Entity with the highest Product Number', function() {
//                var rows;
//                var erg = service.getNextValidProductNumber();
//
//                erg.$promise.then(function(data) {
//                    rows = data;
//                });
//                $httpBackend.flush();
//
//                expect(rows).toBeDefined();
//            });
//
//            it('should return all stored Products of the Database', function() {
//                var products;
//
//                var promise = service.findAll();
//
//                promise.$promise.then(function(data) {
//                    products = data;
//                });
//                $httpBackend.flush();
//
//                expect(products).toBeDefined();
//                expect(products.length).toBe(2);
//            });
//
//            it('should save a new Product', function() {
//
//                var promise = service.save({save: function() {}})
//
//                expect(promise.$promise.then()).toBeDefined();
//            });
//
//            it('should delete an existing Product', function() {
//                var promise = service.deleteByProductNumber("ASDFASDFASDF");
//
//                expect(promise.$promise).toBeDefined();
//            });
//
//            it('should search and find products', function() {
//                var products;
//
//                var promise = service.searchProduct()
//
//                promise.$promise.then(function(data) {
//                    products = data;
//                });
//
//                $httpBackend.flush();
//                expect(products).toBeDefined();
//                expect(products.length).toBe(2)
//            });
//        })
//    });
//});
