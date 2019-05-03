//# sourceURL=pos.strategyConfig.js
require.config({
    paths: {
        'pos.abstractStrategyFactory': 'components/strategies/abstractStrategyFactory',

        'pos.orderOpenedStrategyFactory': 'components/strategies/states/orderOpenedStrategyFactory',
        'pos.subtotalCalculatedStrategyFactory': 'components/strategies/states/subtotalCalculatedStrategyFactory',
        'pos.loggedOutStrategyFactory': 'components/strategies/states/loggedOutStrategyFactory',
        'pos.deviceNotSetupStrategyFactory': 'components/strategies/states/deviceNotSetupStrategyFactory',
        'pos.enterDiscountAmountStrategyFactory': 'components/strategies/states/enterDiscountAmountStrategyFactory',
        'pos.enterDiscountPercentStrategyFactory': 'components/strategies/states/enterDiscountPercentStrategyFactory',
        'pos.cashbookAmountStrategyFactory': 'components/strategies/states/cashbookAmountStrategyFactory',
        'pos.cashbookDescriptionStrategyFactory': 'components/strategies/states/cashbookDescriptionStrategyFactory',
        'pos.productCustomPriceStrategyFactory': 'components/strategies/states/productCustomPriceStrategyFactory',
        'pos.doNothingStrategyFactory': 'components/strategies/states/doNothingStrategyFactory',
        'pos.moveOrderStrategyFactory': 'components/strategies/states/moveOrderStrategyFactory',
        'pos.reassignOrderStrategyFactory': 'components/strategies/states/reassignOrderStrategyFactory',
        'pos.splitOrderStrategyFactory': 'components/strategies/states/splitOrderStrategyFactory',
        'pos.splitPaymentStrategyFactory': 'components/strategies/states/splitPaymentStrategyFactory',
        'pos.openOrderStrategyFactory': 'components/strategies/states/openOrderStrategyFactory',
        'pos.invoiceCreatedStrategyFactory': 'components/strategies/states/invoiceCreatedStrategyFactory',

        'pos.keyboardInputStrategy': 'components/strategies/numberInput/keyboardInputStrategy',
        'pos.numpadInputStrategy': 'components/strategies/numberInput/numpadInputStrategy',
        'pos.productCustomPriceInputStrategy': 'components/strategies/numberInput/productCustomPriceInputStrategy',
        'pos.subtotalCalculatedInputStrategy': 'components/strategies/numberInput/subtotalCalculatedInputStrategy',
        'pos.percentInputStrategy': 'components/strategies/numberInput/percentInputStrategy',
        'pos.quantityInputStrategy': 'components/strategies/numberInput/quantityInputStrategy',
        'pos.doNothingInputStrategy': 'components/strategies/numberInput/doNothingInputStrategy',
        'pos.defaultInputStrategy': 'components/strategies/numberInput/defaultInputStrategy',
        'pos.invoiceCreatedInputStrategy': 'components/strategies/numberInput/invoiceCreatedInputStrategy',
        'pos.cashbookAmountInputStrategy': 'components/strategies/numberInput/cashbookAmountInputStrategy',
        'pos.cashbookDescriptionInputStrategy': 'components/strategies/numberInput/cashbookDescriptionInputStrategy',

        'pos.cashBookAmountStrategy': 'components/strategies/enterButton/cashBookAmountStrategy',
        'pos.cashBookDescriptionStrategy': 'components/strategies/enterButton/cashBookDescriptionStrategy',
        'pos.enterDiscountStrategy': 'components/strategies/enterButton/enterDiscountStrategy',
        'pos.productCustomPriceEnterStrategy': 'components/strategies/enterButton/productCustomPriceEnterStrategy',
        'pos.doNothingEnterStrategy': 'components/strategies/enterButton/doNothingEnterStrategy',
        'pos.splitOrderEnterStrategy': 'components/strategies/enterButton/splitOrderEnterStrategy',
        'pos.loggedOutStrategy': 'components/strategies/enterButton/loggedOutStrategy',
        'pos.deviceNotSetupStrategy': 'components/strategies/enterButton/deviceNotSetupStrategy',

        'pos.splitOrderChooseOrderItemStrategy': 'components/strategies/chooseOrderItem/splitOrderChooseOrderItemStrategy',
        'pos.splitPaymentChooseOrderItemStrategy': 'components/strategies/chooseOrderItem/splitPaymentChooseOrderItemStrategy',
        'pos.defaultChooseOrderItemStrategy': 'components/strategies/chooseOrderItem/defaultChooseOrderItemStrategy',

        'pos.reassignOrderChooseUserStrategy': 'components/strategies/chooseUser/reassignOrderChooseUserStrategy',
        'pos.defaultChooseUserStrategy': 'components/strategies/chooseUser/defaultChooseUserStrategy',

        'pos.moveOrderOpenOrderStrategy': 'components/strategies/openOrder/moveOrderOpenOrderStrategy',
        'pos.defaultOpenOrderStrategy': 'components/strategies/openOrder/defaultOpenOrderStrategy',

        'pos.openOrderChooseOrderStrategy': 'components/strategies/chooseOrder/openOrderChooseOrderStrategy',
        'pos.defaultChooseOrderStrategy': 'components/strategies/chooseOrder/defaultChooseOrderStrategy',

        'pos.doNothingFilter': 'components/strategies/inputFilter/doNothingFilter',
        'pos.percentFilter': 'components/strategies/inputFilter/percentFilter',
        'pos.priceFilter': 'components/strategies/inputFilter/priceFilter',
        'pos.numberFilter': 'components/strategies/inputFilter/numberFilter',
        'pos.everyThingFilter': 'components/strategies/inputFilter/everyThingFilter',
    }
});
define([
    'angular',
    'pos.abstractStrategyFactory'
], function () {
    return function (module) {
        module.service("pos.abstractStrategyFactory", require("pos.abstractStrategyFactory"));
    }
});