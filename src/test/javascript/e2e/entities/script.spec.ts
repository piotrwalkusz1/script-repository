import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Script e2e test', () => {

    let navBarPage: NavBarPage;
    let scriptDialogPage: ScriptDialogPage;
    let scriptComponentsPage: ScriptComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Scripts', () => {
        navBarPage.goToEntity('script');
        scriptComponentsPage = new ScriptComponentsPage();
        expect(scriptComponentsPage.getTitle())
            .toMatch(/scriptRepositoryApp.script.home.title/);

    });

    it('should load create Script dialog', () => {
        scriptComponentsPage.clickOnCreateButton();
        scriptDialogPage = new ScriptDialogPage();
        expect(scriptDialogPage.getModalTitle())
            .toMatch(/scriptRepositoryApp.script.home.createOrEditLabel/);
        scriptDialogPage.close();
    });

   /* it('should create and save Scripts', () => {
        scriptComponentsPage.clickOnCreateButton();
        scriptDialogPage.setNameInput('name');
        expect(scriptDialogPage.getNameInput()).toMatch('name');
        scriptDialogPage.setDescriptionInput('description');
        expect(scriptDialogPage.getDescriptionInput()).toMatch('description');
        scriptDialogPage.scriptLanguageSelectLastOption();
        scriptDialogPage.setCodeInput('code');
        expect(scriptDialogPage.getCodeInput()).toMatch('code');
        scriptDialogPage.setDownloadCountInput('5');
        expect(scriptDialogPage.getDownloadCountInput()).toMatch('5');
        scriptDialogPage.collectionSelectLastOption();
        // scriptDialogPage.tagsSelectLastOption();
        scriptDialogPage.save();
        expect(scriptDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ScriptComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-script div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ScriptDialogPage {
    modalTitle = element(by.css('h4#myScriptLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    descriptionInput = element(by.css('input#field_description'));
    scriptLanguageSelect = element(by.css('select#field_scriptLanguage'));
    codeInput = element(by.css('textarea#field_code'));
    downloadCountInput = element(by.css('input#field_downloadCount'));
    collectionSelect = element(by.css('select#field_collection'));
    tagsSelect = element(by.css('select#field_tags'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    };

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    };

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    };

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
    };

    setScriptLanguageSelect = function(scriptLanguage) {
        this.scriptLanguageSelect.sendKeys(scriptLanguage);
    };

    getScriptLanguageSelect = function() {
        return this.scriptLanguageSelect.element(by.css('option:checked')).getText();
    };

    scriptLanguageSelectLastOption = function() {
        this.scriptLanguageSelect.all(by.tagName('option')).last().click();
    };
    setCodeInput = function(code) {
        this.codeInput.sendKeys(code);
    };

    getCodeInput = function() {
        return this.codeInput.getAttribute('value');
    };

    setDownloadCountInput = function(downloadCount) {
        this.downloadCountInput.sendKeys(downloadCount);
    };

    getDownloadCountInput = function() {
        return this.downloadCountInput.getAttribute('value');
    };

    collectionSelectLastOption = function() {
        this.collectionSelect.all(by.tagName('option')).last().click();
    };

    collectionSelectOption = function(option) {
        this.collectionSelect.sendKeys(option);
    };

    getCollectionSelect = function() {
        return this.collectionSelect;
    };

    getCollectionSelectedOption = function() {
        return this.collectionSelect.element(by.css('option:checked')).getText();
    };

    tagsSelectLastOption = function() {
        this.tagsSelect.all(by.tagName('option')).last().click();
    };

    tagsSelectOption = function(option) {
        this.tagsSelect.sendKeys(option);
    };

    getTagsSelect = function() {
        return this.tagsSelect;
    };

    getTagsSelectedOption = function() {
        return this.tagsSelect.element(by.css('option:checked')).getText();
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
