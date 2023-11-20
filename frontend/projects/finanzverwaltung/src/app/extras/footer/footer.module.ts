import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FooterComponent } from './footer.component';



const routes: Routes = [
  { path: 'footer', component: FooterComponent,}
];
@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),

  ]
})
export class FooterModule { }
