import { NgModule, ViewChild, isStandalone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { OverviewComponent } from './overview.component';
import { NgApexchartsModule } from "ng-apexcharts";
import { SideBarComponent } from '../side-bar/side-bar.component';





const routes: Routes = [
  { path: 'overview', component: OverviewComponent
}
]

@NgModule({
  declarations: [
    OverviewComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    NgApexchartsModule,
    SideBarComponent


    
  ]
})
export class OverviewModule {


  }
  
