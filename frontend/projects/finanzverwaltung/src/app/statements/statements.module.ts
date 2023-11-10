import { NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { StatementsComponent } from './statements.component';
import { AppComponent } from '../app.component';
import { SideBarComponent } from '../side-bar/side-bar.component';

const routes: Routes = [
  { path: 'hallo', component: StatementsComponent
}
]

@NgModule({
  declarations: [
    StatementsComponent,
    
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SideBarComponent

  ],
})
export class StatementsModule {}
