import { Component } from "@angular/core";
import * as ApexCharts from 'apexcharts';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
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
export class AnalyticsComponent {
  protected columnChartData: Partial<ChartData>;
  protected balkenChartData: Partial<ChartData>;
  protected lineChartData: Partial<ChartData>;

  protected currentBankAccount: string = "all";
  protected bankAccounts?: BankAccount[];

  protected expenses = 0;
  protected income = 0;

  protected dateRange = new FormGroup({
    start: new FormControl(new Date(year, month - 1, 1)),
    end: new FormControl(new Date(year, month, 0)),
  });

  constructor(public chartSettings: ChartSettingsModule, private toastr: ToastrService, private requestService: RequestService, private bankAccountService: BankAccountService) {

    this.bankAccountService.currentBankAccount$.subscribe((chosenBankAccount) => {
      this.currentBankAccount = chosenBankAccount;
      this.loadData();
    });

    this.bankAccountService.bankAccounts$.subscribe((bankAccounts) => {
      this.bankAccounts = bankAccounts;
    });

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
        colors: [function({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
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
        colors: [function({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
          if (value < 0) {
            return '#fd0606'
          } else {
            return '#06fd1b'
          }
        }]
      },
    };
  }

  changeBankAccount(bankAccount: string) {
    this.bankAccountService.changeBankAccount(bankAccount);
  }

  changeDate() {
    const startEndDate = this.dateRange.value;
    if(startEndDate.end == undefined || startEndDate.start == undefined){
      this.toastr.warning("Ausgewählter Zeitraum ist ungültig: " + startEndDate.start?.toLocaleDateString('de-DE') + " bis " + startEndDate.end?.toLocaleDateString('de-DE'));
      return;
    }
    this.loadData();
    this.toastr.success("Ausgewählter Zeitraum: " + startEndDate.start?.toLocaleDateString('de-DE') + " bis " + startEndDate.end?.toLocaleDateString('de-DE'));
  }

  loadData() {
    this.requestService.post("transactions/query-transactions", {
      "id": this.currentBankAccount,
      "start": this.dateRange.value.start?.getTime(),
      "end": this.dateRange.value.end?.getTime(),
    }).subscribe(response => {
      this.updateData(response);
    });
  }

  updateData(data: any) {
    this.income = data.income.toFixed(2);
    this.expenses = data.expenses.toFixed(2);

    this.lineChartData.title = { text: "Einnahmen/Ausgaben" };
    this.lineChartData.series = [{
      name: "Einnahmen",
      data: data.dailyIncome.map((transaction: any) => transaction.amount.toFixed(2))
    },
    {
      name: "Ausgaben",
      data: data.dailyExpenses.map((transaction: any) => transaction.amount.toFixed(2)),
    }];
    this.lineChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    }

    this.columnChartData.title = {
      text: "Einnahmen/Ausgaben",
    }
    this.columnChartData.series = [{
      name: "Einnahmen/Ausgaben",
      data: data.dailyFinances.map((transaction: any) => transaction.amount.toFixed(2)),
    }];
    this.columnChartData.xaxis = {
      categories: data.dailyFinances.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
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
}
