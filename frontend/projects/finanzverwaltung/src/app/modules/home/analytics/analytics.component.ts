import { AuthService } from '../../../core/services/auth.service';
import { RegisterComponent } from '../../auth/register/register.component';
import { LoginComponent } from '../../auth/login/login.component';
import { OverviewComponent } from '../overview/overview.component';
import { Component, OnInit, ViewChild } from "@angular/core";
import * as ApexCharts from 'apexcharts';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
  ApexStroke,
  ApexGrid,
  ApexPlotOptions,
  ApexYAxis,
  ApexFill
} from "ng-apexcharts";
import { FormControl, FormGroup } from '@angular/forms';
import { ChartSettingsModule } from '../../../core/chartSettings/chartSettings.module';
import { ToastrService } from 'ngx-toastr';
import { RequestService } from '../../../core/services/request.service';
import { BankAccount, BankAccountService } from '../../../core/services/bankAccountService';


export type ChartData = {
  series: ApexAxisChartSeries;
  donut_data: ApexNonAxisChartSeries;
  xaxis: ApexXAxis;
  grid: ApexGrid;
  title: ApexTitleSubtitle;
  chart: ApexChart;
  plotOptions: ApexPlotOptions;
  dataLabels: ApexDataLabels;
  fill: ApexFill;
  yaxis: ApexYAxis;

};
const today = new Date();
const month = today.getMonth();
const year = today.getFullYear();

@Component({
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss']
})

export class AnalyticsComponent implements OnInit {
  @ViewChild("chart") chart!: ChartComponent;
  public lineChartData: Partial<ChartData>;
  public columnChartData: Partial<ChartData>;
  public balkenChartData: Partial<ChartData>;
  public bankAccounts?: BankAccount[];
  public currentBankAccount: string = "all";
  income = 0;
  expenses = 0;
  
  campaignOne = new FormGroup({
    start: new FormControl(new Date(year, month, today.getDate())),
    end: new FormControl(new Date(year, month, today.getDate() + 1)),
  });

  changeDate() {
    this.loadData();
    const startEndDate = this.campaignOne.value;
    this.toastr.success("Ausgewählter Zeitraum: " + startEndDate.start?.toLocaleDateString('de-DE') + " bis " + startEndDate.end?.toLocaleDateString('de-DE'));
  }
  loadData() {
    this.requestService.post("transactions/query-transactions", {
      "id": this.currentBankAccount,
      "start": this.campaignOne.value.start?.getTime(),
      "end": this.campaignOne.value.end?.getTime(),
    }).subscribe(response => {
      this.updateData(response);

    });
  }
  updateData(data: any) {
    console.log(data);
    this.income = data.income.toFixed(2);
    this.expenses = data.expenses.toFixed(2);
    const startDate = this.campaignOne.value.start;

    this.lineChartData.title = { text: "Einnahmen/Ausgaben Übersicht für: " + startDate?.toLocaleString('de-DE', { month: 'long' }) };
    this.lineChartData.series = [{
      name: "Einnahmen",
      data: data.transactions.map((transaction: any) => {
        var amount = transaction.amount.toFixed(2);	
        if(amount >= 0){
          return amount;
        }
        return 0;
      }),
    },
    {
      name: "Ausgaben",
      data: data.transactions.map((transaction: any) => {
        var amount = transaction.amount.toFixed(2);	
        if(amount <= 0){
          return amount;
        }
        return 0;
      }),
    },
  ];

    this.lineChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    }

    this.columnChartData.title = {
      text: "Einnahmen/Ausgaben Übersicht für: " + startDate?.toLocaleString('de-DE', { month: 'long' }),
    }

    this.columnChartData.series = [{
      name: "Einnahmen/Ausgaben",
      data: data.dailyExpenses.map((transaction: any) => transaction.amount.toFixed(2)),
    }];
    this.columnChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    }
    this.balkenChartData.series = [{
      name: "basic",
      data: data.categoryExpenses.map((category: any) => category.amount.toFixed(2))
    }];

    this.balkenChartData.xaxis = {
      categories: data.categoryExpenses.map((category: any) => {
        var categoryName = category.category?.name;
        if (categoryName == null) {
          return "Nicht Kategorisiert";
        }
        return categoryName;
      })
    };
  }
  
  changeBankAccount(bankAccount:string){
    this.bankAccountService.changeBankAccount(bankAccount);
  }
  ngOnInit() {
    this.bankAccountService.currentBankAccount$.subscribe((chosenBankAccount) => {
      this.currentBankAccount = chosenBankAccount;
      this.loadData();
    });
    
    this.bankAccountService.bankAccounts$.subscribe((bankAccounts) => {
      this.bankAccounts = bankAccounts;
    });
  }
  constructor(public chartSettings: ChartSettingsModule, private toastr: ToastrService, private requestService: RequestService, private bankAccountService: BankAccountService) {


    this.lineChartData = {};

    this.columnChartData = {
      chart: {
        height: 280,
        type: "bar",
        zoom: {
          enabled: false
        }
      },
      title: {
        floating: false,
        offsetY: 20,
        align: "center",
        style: {
          color: "#444"
        }
      },
      plotOptions: {
        bar: {
          dataLabels: {
            position: "top"
          }
        }
      },
      dataLabels: {
        enabled: true,
        offsetY: -20,
        style: {
          fontSize: "12px",
          colors: ["#212121"]
        }
      },
      fill: {
        colors: [function ({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
          if (value < 0) {
            return '#fd0606'
          } else {
            return '#06fd1b'
          }
        }]
      },
      yaxis: {
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        labels: {
          show: false,
        }
      },
    };

    this.balkenChartData = {
      chart: {
        type: "bar",
        height: 350
      },
      plotOptions: {
        bar: {
          horizontal: true
        }
      },
      dataLabels: {
        enabled: false
      },
      fill: {
        colors: [function ({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
          if (value < 0) {
            return '#fd0606'
          } else {
            return '#06fd1b'
          }
        }]
      },
    };
    this.loadData();
  }
}

