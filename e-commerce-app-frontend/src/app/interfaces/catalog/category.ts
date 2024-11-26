export interface Category {
    id?: string;
    name?: string;
    providerCode?: string;
    parentId?: string;
    parentName?: string;
    categories ?: Category[];
    categoryId?: string;
    status?: string;
    updatedAt?: string;
    createdAt?: string;
    createdBy?: string;    
}