import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { User, UserAddress } from 'src/app/interfaces/users/user';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private apiProfileUrl : string;
  private profileInformation = "/user";
  private addressSufixPath   = "/address";
  private addressesSufixPath = "/addresses";
  private paymentSufixPath   = "/payment-method"
  private paymentsSufixPath  = "/payment-methods";

  constructor(
    private http: HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
  ) {
    this.apiProfileUrl = config.API_URL + this.profileInformation;
   }

  getProfileInformation(user : User): Observable<any> {
      return this.http.get<Response<any>>(this.apiProfileUrl+"/"+user.id).pipe(
        catchError(this.utils.handleError)
      );
  }

  updateProfileInformation(user : User): Observable<any> {
    return this.http.patch<Response<any>>(this.apiProfileUrl+"/"+user.id , user).pipe(
      catchError(this.utils.handleError)
    );
  }

  getProfileAddressInformation(user : User): Observable<any> {
    return this.http.get<Response<any>>(this.apiProfileUrl+"/"+user.id+this.addressesSufixPath).pipe(
      catchError(this.utils.handleError)
    );
  }

  createProfileAddressInformation(userId : string, address: UserAddress): Observable<any> {
    const addressPayload = {
      addresses: [
        {
          country: address.country,
          city   : address.city,
          address: address.address,
          zipcode: address.zipcode,
          isDefault: address.isDefault,
          coordinates : address.coordinates,
          region : address.region
        }
      ]
    };   
    return this.http.post<Response<any>>(this.apiProfileUrl + "/" + userId + this.addressSufixPath, addressPayload).pipe(
      catchError(this.utils.handleError)
    );
  }

  updateProfileAddressInformation(userId : string, address: UserAddress): Observable<any> {   
    return this.http.patch<Response<any>>(this.apiProfileUrl + "/" + userId + this.addressSufixPath + "/" + address.id , address).pipe(
      catchError(this.utils.handleError)
    );
  }

  deleteProfileAddressInformation(userId: string, addressId: string): Observable<any> {
    return this.http.delete<Response<any>>(this.apiProfileUrl + "/" + userId + this.addressSufixPath + "/" + addressId).pipe(
      catchError(this.utils.handleError)
    );
  }

  getProfilePaymentInformation(user : User): Observable<any> {
    return this.http.get<Response<any>>(this.apiProfileUrl+"/"+user.id+this.paymentsSufixPath).pipe(
      catchError(this.utils.handleError)
    );
  }

  createProfilePaymentInformation(userId: string, payment: any): Observable<any> {
    const paymentPayload = {
      payments: [{
      type: payment.type,
      accountNumber: payment.accountNumber.replace(/\s+/g, ''),
      expirationDate: payment.expirationDate,
      isDefault: payment.isDefault
      }]
    };
    return this.http.post<Response<any>>(this.apiProfileUrl + "/" + userId + this.paymentSufixPath, paymentPayload).pipe(
      catchError(this.utils.handleError)
    );
  }

  updateProfilePaymentInformation(userId: string, payment: any): Observable<any> {
    payment.accountNumber =  payment.accountNumber.replace(/\s+/g, '');
    return this.http.patch<Response<any>>(this.apiProfileUrl + "/" + userId + this.paymentSufixPath + "/" + payment.id, payment).pipe(
      catchError(this.utils.handleError)
    );
  }

  deleteProfilePaymentInformation(userId: string, paymentId: string): Observable<any> {
    return this.http.delete<Response<any>>(this.apiProfileUrl + "/" + userId + this.paymentSufixPath + "/" + paymentId).pipe(
      catchError(this.utils.handleError)
    );
  }

}
