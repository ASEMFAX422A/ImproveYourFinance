import { NgModule, ViewChild, isStandalone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { OverviewComponent } from './overview.component';






const routes: Routes = [
  { path: 'overview', component: OverviewComponent
}
]

@NgModule({
  declarations: [
    OverviewComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),

    
  ]
})
export class OverviewModule {
  
 }
