export interface Login {
    username?:   string;   
    password?:   string;   
    accessToken?:string;
    refreshToken?:string;
    _val?:boolean; 
    _exp?:boolean;
    _dat?:LoginUserData;
}

export interface LoginUserData {
    username: string;
    firstName: string;
    lastName: string;
    roles: string[];
}