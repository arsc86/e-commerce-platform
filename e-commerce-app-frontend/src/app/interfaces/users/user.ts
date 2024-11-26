import { Role } from "./role";

export interface User {
    id?       : string;
    firstName?: string;
    lastName? : string;
    username? : string; 
    password? : string;
    status?   : string;
    createdAt?: string;
    updatedAt?: string;
    roles?    : any;
    role?     : string;
    byAdmin?  : boolean;
    profile?  : UserProfile;
  }

  export interface UserProfile{
    email?       : string;
    providerCode?: string;
    phone?       : string;
    birthday?    : string;
    imageProfile?: string;
  } 

  export interface UserAddress{
    id?          : string;
    region?      : string;
    country?     : string;
    city?        : string;
    address?     : string;
    zipcode?     : string;
    coordinates? : string;
    isDefault?   : string;
    createdAt?   : string;
    updatedAt?   : string;
    status?      : string;
  }

  export interface UserPaymentMethod{
    id?          : string;
    type?        : string;
    accountNumber?: string;
    expirationDate?: string;
    isDefault?   : string;
    createdAt?   : string;
    updatedAt?   : string;
    status?      : string;
  }
