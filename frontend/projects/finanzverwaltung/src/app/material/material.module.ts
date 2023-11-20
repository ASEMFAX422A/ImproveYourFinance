import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NgIf} from '@angular/common';

import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';


import {FormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';

const MaterialComponent = [
  MatButtonModule,
  MatMenuModule,
  MatIconModule,
  MatSidenavModule,
  NgIf,
  MatDialogModule,
  MatFormFieldModule,
  FormsModule,
  MatInputModule

];
@NgModule({

  imports: [CommonModule,MaterialComponent],
  exports:[MaterialComponent]
})
export class MaterialModule { }
