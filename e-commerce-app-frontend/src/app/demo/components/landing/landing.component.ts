import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LayoutService } from 'src/app/layout/service/app.layout.service';

@Component({
    selector: 'app-landing',
    templateUrl: './landing.component.html',
    standalone: false
})
export class LandingComponent {

    constructor(public layoutService: LayoutService, public router: Router) { }
    
}
