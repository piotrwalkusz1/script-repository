import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Collection e2e test', () => {

    let navBarPage: NavBarPage;
    let collectionDialogPage: CollectionDialogPage;
    let collectionComponentsPage: CollectionComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Collections', () => {
        navBarPage.goToEntity('collection');
        collectionComponentsPage = new CollectionComponentsPage();
        expect(collectionComponentsPage.getTitle())
            .toMatch(/scriptRepositoryApp.collection.home.title/);

    });

    it('should load create Collection dialog', () => {
        collectionComponentsPage.clickOnCreateButton();
        collectionDialogPage = new CollectionDialogPage();
        expect(collectionDialogPage.getModalTitle())
            .toMatch(/scriptRepositoryApp.collection.home.createOrEditLabel/);
        collectionDialogPage.close();
    });

   /* it('should create and save Collections', () => {
        collectionComponentsPage.clickOnCreateButton();
        collectionDialogPage.setNameInput('name');
        expect(collectionDialogPage.getNameInput()).toMatch('name');
        collectionDialogPage.privacySelectLastOption();
        collectionDialogPage.ownerSelectLastOption();
        // collectionDialogPage.sharedUsersSelectLastOption();
        collectionDialogPage.save();
        expect(collectionDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CollectionComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-collection div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CollectionDialogPage {
    modalTitle = element(by.css('h4#myCollectionLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    privacySelect = element(by.css('select#field_privacy'));
    ownerSelect = element(by.css('select#field_owner'));
    sharedUsersSelect = element(by.css('select#field_sharedUsers'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    };

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    };

    setPrivacySelect = function(privacy) {
        this.privacySelect.sendKeys(privacy);
    };

    getPrivacySelect = function() {
        return this.privacySelect.element(by.css('option:checked')).getText();
    };

    privacySelectLastOption = function() {
        this.privacySelect.all(by.tagName('option')).last().click();
    };
    ownerSelectLastOption = function() {
        this.ownerSelect.all(by.tagName('option')).last().click();
    };

    ownerSelectOption = function(option) {
        this.ownerSelect.sendKeys(option);
    };

    getOwnerSelect = function() {
        return this.ownerSelect;
    };

    getOwnerSelectedOption = function() {
        return this.ownerSelect.element(by.css('option:checked')).getText();
    };

    sharedUsersSelectLastOption = function() {
        this.sharedUsersSelect.all(by.tagName('option')).last().click();
    };

    sharedUsersSelectOption = function(option) {
        this.sharedUsersSelect.sendKeys(option);
    };

    getSharedUsersSelect = function() {
        return this.sharedUsersSelect;
    };

    getSharedUsersSelectedOption = function() {
        return this.sharedUsersSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
