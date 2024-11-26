import { Component, OnInit } from '@angular/core';
import { MenuItem, SelectItem } from 'primeng/api';
import { User, UserAddress, UserPaymentMethod } from 'src/app/interfaces/users/user';
import { UserUtilService } from 'src/app/interfaces/users/user-util.service';
import { ProfileService } from './profile.service';
import { Response } from 'src/app/interfaces/general/response';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { Constants } from 'src/app/interfaces/general/constants';
import { HttpStatusCode } from '@angular/common/http';
import { UtilsService } from 'src/app/utils/utils.service';
import { Router } from '@angular/router';
import { LocationService } from 'src/app/general/location.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  profileMenu: MenuItem[] = [];

  response : Response<any>;

  //Entities
  user: User                           = this.userUtil.initializeUser();
  userAddress   : UserAddress          = this.userUtil.initializeUserAddress();
  userPayment   : UserPaymentMethod    = this.userUtil.initializeUserPaymentMethod();
  userAddresses : UserAddress[]        = [];
  selectedUserAddresses: UserAddress[] = [];
  userPayments  : UserPaymentMethod[]  = [];
  selectedUserPaymentMethod : UserPaymentMethod[] = [];
  
  //places
  allCountries : any[] = [];
  countries    : any[] = [];
  allCities    : any[] = [];
  cities       : SelectItem[] = [];
  regions      : SelectItem[] = [];

  //payment
  paymentTypes: SelectItem[] = [];

  isEnable : boolean = false;
  isEnableCountryDrop : boolean = true;
  isEnableCityDrop    : boolean = true;
  isEnableCityText    : boolean = false;

  enableAddressProfile : boolean = false;
  isSubmited           : boolean = false;
  enablePaymentProfile : boolean = false;
  deleteDialog         : boolean = false;

  isEditAddressProfile   : boolean = false;
  isEditPaymentProfile   : boolean = false;
  isDeleteAddressProfile : boolean = false;
  isDeletePaymentProfile : boolean = false;

  isValidExpirationDate = false;

  //table configuration
  loading: boolean   = false;
  cols: any[]        = [];
  rowsPerPageOptions = [5, 10, 20];
  rows               = 10;

  //Edit variables
  editFirstNameValue : boolean = false;
  editLastNameValue  : boolean = false;
  editEmailValue     : boolean = false;
  editPhoneValue     : boolean = false;
  editBirthdayValue  : boolean = false;

  firstNameEditValue : string = '';
  lastNameEditValue  : string = '';
  emilEditValue      : string = '';
  phoneEditValue     : string = '';
  birthdayEditValue  : string = '';

  //google maps
  center = { 
    lat: environment.BASE_COORDINATES.lat, 
    lng: environment.BASE_COORDINATES.lng 
  };
  zoom   = 12;
  selectedLocation: { lat: number; lng: number } | null = null;
  viewMap         : boolean = false;
  sessionedUserData: User;
  
  constructor(
    private userUtil             : UserUtilService,
    private profileService       : ProfileService,
    private sharedMessageService : SharedMessageService,
    private utilService          : UtilsService,
    private router               : Router,
    private locationService      : LocationService,
    private userUtilService      : UserUtilService,
  ) {    
  }

  ngOnInit(): void {

    this.locationService.getRegion().subscribe({
      next: (data) => {
          this.response = data;                         
          if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {                            
            this.regions = this.response.payload.map(region => {
              return { label: region.name, value: region.name };
            });
          }
      },
      error: (error) => {                                     
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting Regions', error);            
      }
  });

    this.locationService.getCountries().then(countries => {
      this.allCountries = countries;     
    });

    this.locationService.getCities().then(cities => {
      this.allCities = cities;    
    });

    this.paymentTypes = [
      { label: 'VISA', value: 'VISA' },
      { label: 'MASTERCARD', value: 'MASTERCARD' }
    ];

    this.profileMenu = [     
      { label: 'Delete Account', icon: 'pi pi-trash' }
    ];

    //load user
    this.getProfileInformation();
                
  }

  getProfileInformation() : void {
    this.profileService.getProfileInformation(JSON.parse(localStorage.getItem('_user_data'))).subscribe({
        next: (data) => {
            this.response = data;                         
            if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {               
              this.user = this.response.payload[0];                                  
            }
        },
        error: (error) => {                                     
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting Profile', error);            
        }
    });
  }

  getProfileAddressInformation() : void{
    this.profileService.getProfileAddressInformation(this.user).subscribe({
      next: (data) => {
          this.response = data;                                   
          if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {               
            this.userAddresses = this.response.payload || [];                                             
          }
      },
      error: (error) => {                                     
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting address information', error);            
      }
    });
  }

  getProfilePaymentInformation() : void{
    this.profileService.getProfilePaymentInformation(this.user).subscribe({
      next: (data) => {
          this.response = data;                                   
          if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {               
            this.userPayments = this.response.payload || [];                                             
          }
      },
      error: (error) => {                                     
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting address information', error);            
      }
    });
  }

  validateEditValues(field: string): boolean {
    switch (field) {
      case 'firstName':
        return this.firstNameEditValue != null && this.firstNameEditValue != '';
      case 'lastName':
        return this.lastNameEditValue != null && this.lastNameEditValue != '';
      case 'email':
        return this.emilEditValue != null && this.emilEditValue != '';
      case 'phone':        
        return this.phoneEditValue != null && this.phoneEditValue != '';
      case 'birthday':
        return this.birthdayEditValue != null && this.birthdayEditValue != '';
      default:
        return false;
    }
  }

  editProfileInformation(editValue : string) : void {
    if(!this.validateEditValues(editValue))
    {
      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 
        'Error Updating Profile Information', 'The value '+editValue+' can not be empty ');            
      return;
    }
    this.isEnable              = true;
    this.user.firstName        = this.firstNameEditValue ? this.firstNameEditValue : this.user.firstName;
    this.user.lastName         = this.lastNameEditValue ? this.lastNameEditValue : this.user.lastName;
    this.user.profile.email    = this.emilEditValue ? this.emilEditValue : this.user.profile.email;
    this.user.profile.phone    = this.phoneEditValue ? this.phoneEditValue : this.user.profile.phone;
    this.user.profile.birthday = this.birthdayEditValue ? this.utilService.formatDateString(this.birthdayEditValue) : this.user.profile.birthday;
    this.profileService.updateProfileInformation(this.user).subscribe({
      next: (data) => {
          this.response = data;                         
          if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {    
            this.isEnable           = false;
            this.editFirstNameValue = false;     
            this.editLastNameValue  = false;
            this.editEmailValue     = false;
            this.editPhoneValue     = false;
            this.editBirthdayValue  = false;    
            //Getting updated data  
            this.user = this.response.payload;                             
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Update Profile', this.response.message);                                  
            //Updating user session information
            this.userUtil.updateUser(this.user);
            this.router.navigate([Constants.ROUTING_PATHS.USER_PROFILE]);
          }
      },
      error: (error) => {                                     
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Updating Profile Information', error);            
      }
  });   
  }

  addProfileAddress(): void {

    this.isSubmited = true;
    if (!this.userAddress.address || !this.userAddress.city || 
        !this.userAddress.region || !this.userAddress.country || 
        !this.userAddress.zipcode || !this.userAddress.coordinates ||
        !this.userAddress.isDefault) 
    {
      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Adding Address', 'All address fields must be filled');
      this.isSubmited = false;
      return;
    }

    if(this.userAddress.id)
    {
      this.profileService.updateProfileAddressInformation(this.user.id, this.userAddress).subscribe({
        next: (data) => {
          this.response = data;
          if (this.response.code == HttpStatusCode.Created && this.response.status === 'OK') {
            this.isSubmited           = false;
            this.enableAddressProfile = false;           
            const index = this.userAddresses.findIndex(address => address.id === this.userAddress.id);           
            if (index !== -1) {
              this.userAddresses[index] = this.response.payload;
            }
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Address Updated', this.response.message);
            this.initializeUserAddress();
          }
        },
        error: (error) => {
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Updating Address', error);
          this.isSubmited = false;
        }
      });
    }
    else
    {
        this.profileService.createProfileAddressInformation(this.user.id, this.userAddress).subscribe({
          next: (data) => {
            this.response = data;
            if (this.response.code == HttpStatusCode.Created && this.response.status === 'OK') {
              this.isSubmited           = false;
              this.enableAddressProfile = false;
              this.isEnableCityDrop     = false;
              this.userAddresses.push(this.response.payload[0]);
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Address Added', this.response.message);
              this.initializeUserAddress();
            }
          },
          error: (error) => {       
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Adding Address', error);
            this.isSubmited = false;
          }
        });
    }
  }

  addPaymentMethod(): void {
    this.isSubmited = true;
    if (!this.userPayment.accountNumber  || !this.userPayment.isDefault || 
        !this.userPayment.expirationDate ||  !this.userPayment.type) 
    {
      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Adding Payment Method', 'All payment fields must be filled');
      this.isSubmited = false;
      return;
    }

    if(this.userPayment.id)
    {
      this.profileService.updateProfilePaymentInformation(this.user.id, this.userPayment).subscribe({
        next: (data) => {
          this.response = data;
          if (this.response.code == HttpStatusCode.Created && this.response.status === 'OK') {
            this.isSubmited = false;
            this.enablePaymentProfile = false;
            const index = this.userPayments.findIndex(payment => payment.id === this.userPayment.id);
            if (index !== -1) {
              this.userPayments[index] = this.response.payload;
            }
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Payment Method Updated', this.response.message);
            this.initializerUserPayment();
          }
        },
        error: (error) => {
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Updating Payment Method', error);
          this.isSubmited = false;
        }
      });
    }
    else
    {
      this.profileService.createProfilePaymentInformation(this.user.id, this.userPayment).subscribe({
        next: (data) => {
          this.response = data;
          if (this.response.code == HttpStatusCode.Created && this.response.status === 'OK') {
            this.isSubmited = false;
            this.enablePaymentProfile = false;
            this.userPayments.push(this.response.payload[0]);
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Payment Method Added', this.response.message);
            this.initializerUserPayment();
          }
        },
        error: (error) => {
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Adding Payment Method', error);
          this.isSubmited = false;
        }
      });
    }
  }

  deleteProfileAddress(userAddress : UserAddress){
    this.userAddress = {...userAddress};  
    this.deleteDialog = true; 
    this.isDeleteAddressProfile = true;
  }

  editProfileAddress(userAddress : UserAddress){
    this.userAddress = {...userAddress};       
    this.isEditAddressProfile = true;
    this.enableAddressProfile = true;
    this.isEnableCountryDrop  = false;
    this.isEnableCityDrop     = false;
    this.countries = this.selectCountries(this.userAddress.region);
    this.cities    = this.selectCities(this.userAddress.country);
  }

  confirmDeleteProfileAddress() {
    this.profileService.deleteProfileAddressInformation(this.user.id, this.userAddress.id).subscribe({
      next: (data) => {
        this.response = data;
        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {          
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Address Deleted', this.response.message);
          this.getProfileAddressInformation();
          this.deleteDialog           = false;
          this.isDeleteAddressProfile = false;
        }
      },
      error: (error) => {
        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Deleting Address', error);
        this.deleteDialog = false;
      }
    });
  }

  deleteProfilePayment(userPayment: UserPaymentMethod) {
    this.userPayment = { ...userPayment };
    this.deleteDialog = true;
    this.isDeletePaymentProfile = true;
  }

  editProfilePayment(userPayment: UserPaymentMethod) {
    this.userPayment = { ...userPayment };
    this.isEditPaymentProfile = true;
    this.enablePaymentProfile = true;
  }

  confirmDeleteProfilePayment() {
    this.profileService.deleteProfilePaymentInformation(this.user.id, this.userPayment.id).subscribe({
      next: (data) => {
        this.response = data;
        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Payment Method Deleted', this.response.message);
          this.getProfilePaymentInformation();
          this.deleteDialog = false;
          this.isDeleteAddressProfile = false;
        }
      },
      error: (error) => {
        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error Deleting Payment Method', error);
        this.deleteDialog = false;
      }
    });
  }
 
  initializeUserAddress(): void {
    this.userAddress = this.userUtil.initializeUserAddress();
  }

  initializerUserPayment() : void{
    this.userPayment = this.userUtil.initializeUserPaymentMethod();
  }

  selectCountries(region : string) : any{   
    return this.allCountries.filter(country => country.region === region).map(city => {           
      return { label: city.label, value: city.label , code: city.code};
    });    
  }

  selectCities(countryName : string) : any{        
    const selectedCountry = this.countries.find(country => country.label === countryName);    
    const selectedCountryCode = selectedCountry ? selectedCountry.code : null; 
    return this.allCities.filter(city => city.code === selectedCountryCode).map(city => {      
      return { label: city.label, value: city.label };
    });   
  }

  onRegionChange(event: any): void {
    const selectedRegionCode = event.value;        
    this.countries = this.selectCountries(selectedRegionCode);    
    if(this.countries.length!=0){
      this.isEnableCountryDrop = false;      
    }
    else {
      this.isEnableCountryDrop = true;
    }
  }

  onCountryChange(event: any): void {            
    this.cities = this.selectCities(event.value);
    if(this.cities.length!=0){
      this.isEnableCityDrop = false;
      this.isEnableCityText = false;
    }
    else {
      this.isEnableCityText = true;
    }
  }

  onTabChange(event: any): void {    
    if(event.index == 1)
    {
      this.getProfileAddressInformation();
    }
    if(event.index == 2)
    {
       this.getProfilePaymentInformation();
    }
  }

  validateNumber(event: KeyboardEvent): void {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        event.preventDefault();
    }
  }

  validateExpirationDate(): void {
    const input = this.userPayment.expirationDate;    
    const regex = /^(0[1-9]|1[0-2])\/?(202[5-9]|20[3-9][0-9])$/;

    if (!regex.test(input)) {
      this.userPayment.expirationDate = '';
      this.isValidExpirationDate = true;
    }
    else this.isValidExpirationDate = false;
  }

  //Google Maps
  onMapClick(event: google.maps.MapMouseEvent) {    
    if (event.latLng) {
      this.selectedLocation = {
        lat: event.latLng.lat(),
        lng: event.latLng.lng()
      };
      this.viewMap = false;
      this.userAddress.coordinates = this.selectedLocation.lat.toFixed(4) + ',' + this.selectedLocation.lng.toFixed(4);
    }
  }

  openMap(): void {
    this.viewMap = true;
  }

}
