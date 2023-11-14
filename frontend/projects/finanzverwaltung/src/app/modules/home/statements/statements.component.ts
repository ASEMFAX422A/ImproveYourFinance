import { HttpClient } from '@angular/common/http';
import {Component} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OverviewComponent } from '../overview/overview.component';
import { AnalyticsComponent } from '../analytics/analytics.component';
import { LandingPageComponent } from '../../landing-page/landing-page.component';
import { LoginComponent } from '../../auth/login/login.component';
import { PdfDialogComponent } from '../../../extras/pdf-dialog/pdf-dialog.component';

@Component({
  selector: 'statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss']
})


export class StatementsComponent {



  constructor(private httpClient: HttpClient,public dialog: MatDialog){}
  
  openDialog(){
    this.dialog.open(PdfDialogComponent)
  }
}
