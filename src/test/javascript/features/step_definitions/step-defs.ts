const { Given, When, Then, Before } = require('cucumber');
const request = require('request-promise');
import { browser, by, $, element } from 'protractor';

let chai = require('chai');
let chaiAsPromised = require('chai-as-promised');
chai.use(chaiAsPromised);
let expect = chai.expect;

const HOST_URL = 'http://localhost:9000';

let currentUserJWT = null;
let currentUser = null;
let adminJWT = null;

let SelectWrapper = function(selector) {
    this.webElement = element(selector);
};
SelectWrapper.prototype.getOptions = function() {
    return this.webElement.all(by.tagName('option'));
};
SelectWrapper.prototype.getSelectedOptions = function() {
    return this.webElement.all(by.css('option[selected="selected"]'));
};
SelectWrapper.prototype.selectByValue = function(value) {
    return this.webElement.all(by.css('option[value="' + value + '"]')).click();
};
SelectWrapper.prototype.selectByPartialText = function(text) {
    return this.webElement.all(by.cssContainingText('option', text)).click();
};
SelectWrapper.prototype.selectByText = function(text) {
    return this.webElement.all(by.xpath('option[.="' + text + '"]')).click();
};

Before((_, callback) => {
    request.post({url: HOST_URL + '/api/authenticate', json: {username: 'admin', password: 'admin'}}).on('data', (data) => {
        adminJWT = JSON.parse(data).id_token;

        request.get(HOST_URL + '/api/collections').auth(null, null, true, adminJWT).on('data', (data) => {
            let collectionIds = JSON.parse(data).map(x => x.id);
            let promises = [];
            for (let collectionId of collectionIds) {
                promises.push(request.delete(HOST_URL + '/api/collections/' + collectionId).auth(null, null, true, adminJWT));
            }
            Promise.all(promises);
            callback();
        });
    });
});

Given('I am on the home page', async () => {
    await browser.get(HOST_URL);
});

Given('I am logged', (callback) => {
    element(by.id('account-menu')).click();
    browser.findElement(by.id('login')).click();
    browser.findElement(by.id('username')).sendKeys('user');
    browser.findElement(by.id('password')).sendKeys('user');
    browser.findElement(by.css('button[type="submit"]')).click().then(() => {
        request.post({url: HOST_URL + '/api/authenticate', json: {"username": "user", "password": "user"}}).on('data', (data) => {
            currentUserJWT = JSON.parse(data).id_token;
            request.get(HOST_URL + '/api/account').auth(null, null, true, currentUserJWT).on('data', (data) => {
                currentUser = JSON.parse(data);
                callback();
            });
        });
    });
});

Given('I hava a private collection with name {string}', (name, callback) => {
    request.post({url: HOST_URL + '/api/collections', json: {name: name, privacy: 'PRIVATE', ownerId: currentUser.id}})
        .auth(null, null, true, adminJWT).on('data', (data) => {
       callback();
    });
});

When('I go to the repository page', async () => {
    await element(by.id('repository-link')).click();
});

When('I choose a collection with name {string}', async (name) => {
    await new SelectWrapper(by.id('collection-list')).selectByText(name);
});


Then('I should see a public collection with name {string}', async (name) => {
    expect(element(by.id('collection-name')).getText()).to.eventually.equal(name);
    await expect(element(by.id('collection-privacy')).getText()).to.eventually.equal('Private');
});


