import { Characteristic } from "./characteristic";

export interface Product {
    id?: number;
    code?: string;
    name?: string;
    description?: string;
    categoryName?: string;
    categoryId?: string;
    provider?: string;
    providerName?: string;
    status?: string;
    createdBy?: string;
    createdAt?: string;
    updatedAt?: string | null;
    stock?: number;
    links?: Link[];
    characteristics?: ProductCharacteristic[];
    prices?: ProductDiscountPrice[];
    discounts?: ProductDiscountPrice[];
    inventory?: ProductStock[];
}

export interface ProductCharacteristic {
    characteristicId?: string;
    characteristicsNames?: string;
    characteristicsValues?: ProductCharacteristicValue[];
    type?: string;
    value?: string;
    id?: string;
}

export interface ProductCharacteristicValue {
    value?: string;
    id?: string;
    file?: File | null;
    fileBase64?: string | null;
    fileName?: string | null;
    links?: Link[];
}

export interface Link {
    rel: string;
    href: string;
}

export interface Pageable {
    pageNumber: number;
    pageSize: number;
    sort: Sort;
    offset: number;
    paged: boolean;
    unpaged: boolean;
}

export interface Sort {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
}

export interface ProductResponse {
    content: Product[];
    pageable: Pageable;
    size: number;
    number: number;
    first: boolean;
    last: boolean;
    sort: Sort;
    numberOfElements: number;
    empty: boolean;
}

export interface ProductCreateRequest {
    product: Product;
    pageNumber: number;
    pageSize: number;
}

export interface ProductDiscountPrice {
    id: number;
    locationId: number;
    location: string;
    value: number;
    startDate: string;
    endDate: string;
    type: string;
    status: string;
    createdAt: string;
}

export interface ProductStock {
    warehouseId: number;
    warehouse: string;
    quantity: number;
}