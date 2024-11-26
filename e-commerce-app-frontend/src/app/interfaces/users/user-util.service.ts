import { Injectable } from '@angular/core';
import { User, UserAddress, UserPaymentMethod, UserProfile } from './user';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserUtilService {

  private userSubject: BehaviorSubject<User>;

  sessionedUserData: any;

  constructor() 
  { 
    const initialUser: User = this.initializeUser();
    this.userSubject = new BehaviorSubject<User>(initialUser);
  }

  initializeUser(): User {
    return {
        id: '',
        firstName: '',
        lastName: '',
        username: '',
        password: '',
        status: 'active',
        createdAt: '',
        updatedAt: '',
        roles: [],
        role: '',
        byAdmin: false,
        profile: this.initializeUserProfile()
    };
  }

  initializeUserProfile(): UserProfile {
    return {
      email: '',
      providerCode: '',
      phone: '',
      birthday: '',
      imageProfile: ''
    };
  }

  initializeUserAddress(): UserAddress {
    return {
      id: '',
      region: '',
      country: '',
      city: '',
      address: '',
      zipcode: '',
      coordinates: '',
      isDefault: '',
      createdAt: '',
      status: ''
    };
  }

  initializeUserPaymentMethod(): UserPaymentMethod {
    return {
      id: '',
      type: '',
      accountNumber: '',
      expirationDate: '',
      isDefault: '',
      createdAt: '',
      updatedAt: '',
      status: ''
    };
  }

  setUser(user: User): void {
    this.userSubject.next(user);
  }

  getUser(): User {
    return this.userSubject.value;
  }

  getUserObservable() {
    return this.userSubject.asObservable();
  }

  updateUser(user: Partial<User>): void {
    const currentUser = this.userSubject.value;
    const updatedUser = { ...currentUser, ...user };
    this.userSubject.next(updatedUser);
  }

  hasRole(sessionedUserData : any, roles: string[]): boolean {    
    if(sessionedUserData?.roles && sessionedUserData.id !== '') {
        return sessionedUserData.roles.some((userRole: { name: string }) => roles.includes(userRole.name));
    }
    return false;
  }

  setLocalStorageUserData(user: User): void {
    let userSession : User = this.initializeUser();
    userSession.id        = user.id;
    userSession.firstName = user.firstName;
    userSession.lastName  = user.lastName;
    userSession.username  = user.username;
    userSession.roles     = user.roles;
    userSession.profile.providerCode = user.profile?.providerCode || '';
    localStorage.setItem('_user_data', JSON.stringify(userSession));
  }

  getSessionInfoByPath(path: string): any {
    const userString = localStorage.getItem('_user_data');
    if (!userString) return null;
    const user = JSON.parse(userString);
    if(path === 'role')
    {
      return user.roles && user.roles.length > 0 ? user.roles[0].name : null;
    }
    return path.split('.').reduce((obj, key) => obj?.[key], user);
  }
}