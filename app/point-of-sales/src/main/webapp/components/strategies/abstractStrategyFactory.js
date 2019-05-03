//# sourceURL=pos.abstractStrategyFactory.js
define([
    'angular',
    'pos.orderOpenedStrategyFactory',
    'pos.subtotalCalculatedStrategyFactory',
    'pos.loggedOutStrategyFactory',
    'pos.deviceNotSetupStrategyFactory',
    'pos.enterDiscountAmountStrategyFactory',
    'pos.enterDiscountPercentStrategyFactory',
    'pos.cashbookAmountStrategyFactory',
    'pos.cashbookDescriptionStrategyFactory',
    'pos.productCustomPriceStrategyFactory',
    'pos.doNothingStrategyFactory',
    'pos.splitOrderStrategyFactory',
    'pos.reassignOrderStrategyFactory',
    'pos.moveOrderStrategyFactory',
    'pos.splitPaymentStrategyFactory',
    'pos.openOrderStrategyFactory',
    'pos.invoiceCreatedStrategyFactory',

    'pos.loggedOutStrategy',
    'pos.deviceNotSetupStrategy',

    "pos.defaultChooseOrderItemStrategy",
    "pos.defaultChooseOrderStrategy",
    "pos.defaultChooseUserStrategy",
    "pos.defaultOpenOrderStrategy",

    "pos.doNothingFilter",
    "pos.percentFilter",
    "pos.priceFilter",
    "pos.numberFilter",
    "pos.everyThingFilter",
], function (angular) {
    return [function () {
        var self = this;
        self.state = "";
        self.activeStrategyFactory = {};
        self.states = {
            orderOpened: "ORDER_OPENED",
            invoiceCreated: "INVOICE_CREATED",
            subtotalCalculated: "SUBTOTAL_CALCULATED",
            loggedOut: "LOGGED_OUT",
            deviceNotSetup: "DEVICE_NOT_SETUP",
            enterDiscountAmount: "ENTER_DISCOUNT_AMOUNT",
            enterDiscountPercent: "ENTER_DISCOUNT_PERCENT",
            cashbookAmountInput: "CASHBOOK_AMOUNT_INPUT",
            cashbookDescriptionInput: "CASHBOOK_DESCRIPTION_INPUT",
            productCustomPrice: "PRODUCT_CUSTOM_PRICE",
            splitOrder: "SPLIT_ORDER",
            reassignOrder: "REASSIGN_ORDER",
            moveOrder: "MOVE_ORDER",
            splitPayment: "SPLIT_PAYMENT",
            doNothing: "DO_NOTHING",
            openOrder: "OPEN_ORDER",
            invoiceCreated: "INVOICE_CREATED"
        };
        this.changeState = function (state) {
            if (state == self.states.orderOpened) {
                self.state = state;
                self.activeStrategyFactory = require("pos.orderOpenedStrategyFactory");
            } else if (state == self.states.invoiceCreated) {
                self.state = state;
                self.activeStrategyFactory = require("pos.invoiceCreatedStrategyFactory");
            } else if (state == self.states.subtotalCalculated) {
                self.state = state;
                self.activeStrategyFactory = require("pos.subtotalCalculatedStrategyFactory");
            } else if (state == self.states.loggedOut) {
                self.state = state;
                self.activeStrategyFactory = require("pos.loggedOutStrategyFactory");
            } else if (state == self.states.deviceNotSetup) {
                self.state = state;
                self.activeStrategyFactory = require("pos.deviceNotSetupStrategyFactory");
            } else if (state == self.states.enterDiscountAmount) {
                self.state = state;
                self.activeStrategyFactory = require("pos.enterDiscountAmountStrategyFactory");
            } else if (state == self.states.enterDiscountPercent) {
                self.state = state;
                self.activeStrategyFactory = require("pos.enterDiscountPercentStrategyFactory");
            } else if (state == self.states.cashbookAmountInput) {
                self.state = state;
                self.activeStrategyFactory = require('pos.cashbookAmountStrategyFactory');
            } else if (state == self.states.cashbookDescriptionInput) {
                self.state = state;
                self.activeStrategyFactory = require("pos.cashbookDescriptionStrategyFactory");
            } else if (state == self.states.productCustomPrice) {
                self.state = state;
                self.activeStrategyFactory = require("pos.productCustomPriceStrategyFactory");
            } else if (state == self.states.doNothing) {
                self.state = state;
                self.activeStrategyFactory = require("pos.doNothingStrategyFactory");
            } else if (state == self.states.splitOrder) {
                self.state = state;
                self.activeStrategyFactory = require("pos.splitOrderStrategyFactory");
            } else if (state == self.states.reassignOrder) {
                self.state = state;
                self.activeStrategyFactory = require("pos.reassignOrderStrategyFactory");
            } else if (state == self.states.moveOrder) {
                self.state = state;
                self.activeStrategyFactory = require("pos.moveOrderStrategyFactory");
            } else if (state == self.states.splitPayment) {
                self.state = state;
                self.activeStrategyFactory = require("pos.splitPaymentStrategyFactory");
            } else if (state == self.states.openOrder) {
                self.state = state;
                self.activeStrategyFactory = require("pos.openOrderStrategyFactory");
            } else if (state == self.states.invoiceCreated) {
                self.state = state;
                self.activeStrategyFactory = require("pos.invoiceCreatedStrategyFactory");
            }
        };
        this.getState = function () {
            return self.state;
        };
        this.createNumberInputStrategy = function () {
            if (self.activeStrategyFactory.createNumberInputStrategy != undefined)
                return self.activeStrategyFactory.createNumberInputStrategy();
            else
                return require("pos.defaultInputStrategy");
        };
        this.createEnterButtonStrategy = function () {
            return self.activeStrategyFactory.createEnterButtonStrategy();
        };
        this.getFilter = function () {
            return self.activeStrategyFactory.getFilter();
        };

        this.chooseOrderItemStrategy = function () {
            if (self.activeStrategyFactory.createChooseOrderItemStrategy != undefined)
                return self.activeStrategyFactory.createChooseOrderItemStrategy();
            else
                return require("pos.defaultChooseOrderItemStrategy");
        };
        this.chooseOrderStrategy = function () {
            if (self.activeStrategyFactory.createChooseOrderStrategy != undefined)
                return self.activeStrategyFactory.createChooseOrderStrategy();
            else
                return require("pos.defaultChooseOrderStrategy");
        };
        this.chooseUserStrategy = function () {
            if (self.activeStrategyFactory.createChooseUserStrategy != undefined)
                return self.activeStrategyFactory.createChooseUserStrategy();
            else
                return require("pos.defaultChooseUserStrategy");
        };
        this.openOrderStrategy = function () {
            if (self.activeStrategyFactory.createOpenOrderStrategy != undefined)
                return self.activeStrategyFactory.createOpenOrderStrategy();
            else
                return require("pos.defaultOpenOrderStrategy");
        };
    }];
});