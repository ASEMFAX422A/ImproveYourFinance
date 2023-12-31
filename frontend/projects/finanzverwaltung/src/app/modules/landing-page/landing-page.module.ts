import { NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { LandingPageComponent } from './landing-page.component';
import { FooterComponent } from '../../extras/footer/footer.component';



const routes: Routes = [
  { path: '', component: LandingPageComponent 
}
]

@NgModule({
  declarations: [
    LandingPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FooterComponent

  ]
})
export class LandingPageModule {}
