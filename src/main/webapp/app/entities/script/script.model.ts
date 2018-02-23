import { BaseEntity } from './../../shared';

export const enum ScriptLanguage {
    'BASH',
    'PYTHON_2',
    'PYTHON_3',
    'GROOVY',
    'KSCRIPT',
    'RUBY'
}

export class Script implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public scriptLanguage?: ScriptLanguage,
        public code?: any,
        public downloadCount?: number,
        public collectionId?: number,
        public tags?: BaseEntity[],
    ) {
    }
}
