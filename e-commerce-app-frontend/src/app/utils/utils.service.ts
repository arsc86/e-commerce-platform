import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { Constants } from '../interfaces/general/constants';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  sessionedUserData: any;

  constructor() {}

  public handleError(error: HttpErrorResponse) {   
    let errorMessage = 'An unknown error occurred!';    
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `A client-side or network error occurred: ${error.error.message}`;
    } else {
      // Backend error           
      let customMessage = error?error.error.message:'';
      let payload       = error?' : '+error.error.payload:'';
      switch (error.status) {
        case 0:
          errorMessage = 'Unable to connect to the server. Please check your internet connection or try again later.';
          break;
        case 400:
          errorMessage = customMessage+payload+' Please verify your input and try again.';
          break;
        case 401:
          errorMessage = customMessage+' Please log in and try again. ';
          break;
        case 403:
          errorMessage = 'Forbidden. You do not have permission to perform this action.';
          break;
        case 404:
          errorMessage = 'Not found. The requested resource could not be found.';
          break;
        case 500:
          errorMessage = 'Internal server error. Please try again later.';
          break;
        case 409:
          errorMessage = customMessage+'. Please try again';
          break;
        default:
          errorMessage = `Unexpected error occurred. Please try again later. (Error code: ${error.status})`;
          break;
      }  
    }
    return throwError(() => new Error(errorMessage));
  }

  //Validate session storage item
  validateSesssionStorageItem(item: string = Constants.SESSION_VAL_TYPES.USER_DATA ): boolean {
    let isValid = false;
    if (sessionStorage.getItem(item) !== null) {
      isValid = true;
    }
    return isValid;
  }

  getSessionStorageItem(item: string = Constants.SESSION_VAL_TYPES.USER_DATA): any {    
    return JSON.parse(sessionStorage.getItem(item));
  }

  clearSessionStorage(): void {
    sessionStorage.clear();
    localStorage.clear();
  }

  formatDate(date: Date): string {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  formatDateString(dateString: string): string {
    const date = new Date(dateString);
    return this.formatDate(date);
  }

}
