import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NgIf} from '@angular/common';

import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';



const MaterialComponent = [
  MatButtonModule,
  MatMenuModule,
  MatIconModule,
  MatSidenavModule,
  NgIf

];
@NgModule({

  imports: [CommonModule,MaterialComponent],
  exports:[MaterialComponent]
})
export class MaterialModule { }
