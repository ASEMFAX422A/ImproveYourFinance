import { AuthService } from '../../../core/services/auth.service';
import { Component } from "@angular/core";
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
import { ChartSettingsModule } from '../../../core/chartSettings/chartSettings.module';
import { RequestService } from '../../../core/services/request.service';

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

@Component({
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent {
  protected columnChartData: Partial<ChartData>;
  protected balkenChartData: Partial<ChartData>;
  protected lineChartData: Partial<ChartData>;

  protected username = this.auth.getUsername();
  protected startBalance = 0;
  protected endBalance = 0;

  constructor(protected chartSettings: ChartSettingsModule, private requestService: RequestService, private auth: AuthService) {

    this.lineChartData = {};
    //TODO Move to ChartSettings
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

    this.loadData();
  }

  getStartDate() {
    let startDate = new Date();
    startDate.setMonth(startDate.getMonth() - 1);
    startDate.setDate(1);
    return startDate;
  }

  getEndDate() {
    let endDate = new Date();
    endDate.setDate(0);
    return endDate;
  }

  loadData() {
    this.requestService.post("bank-account/overview", {
      "id": "all",
      "start": this.getStartDate().getTime(),
      "end": this.getEndDate().getTime(),
    }).subscribe(response => {
      this.updateData(response);
    });
  }

  updateData(data: any) {
    this.startBalance = data.startBalance.toFixed(2);
    this.endBalance = data.endBalance.toFixed(2);

    var balance = data.startBalance;

    this.lineChartData.title = { text: "Verlauf für " + this.getStartDate().toLocaleString('de-DE', { month: 'long' }) };
    this.lineChartData.series = [{
      name: "Einnahmen/Ausgaben",
      data: data.dailyExpenses.map((transaction: any) => {
        balance += transaction.amount;
        return balance.toFixed(2);
      }),
    }];

    this.lineChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    };

    this.columnChartData.title = {
      text: "Einnahmen/Ausgaben für " + this.getStartDate().toLocaleString('de-DE', { month: 'long' }),
    };

    this.columnChartData.series = [{
      name: "Einnahmen/Ausgaben",
      data: data.dailyExpenses.map((transaction: any) => transaction.amount.toFixed(2)),
    }];

    this.columnChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    };

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
