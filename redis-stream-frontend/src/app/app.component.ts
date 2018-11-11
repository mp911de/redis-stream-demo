import {Component, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

@Component({
  selector: 'spf-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
@Injectable()
export class AppComponent {

  options = [new PollOption('streams', 'Streams'), new PollOption('zpop', 'ZPOP'), new PollOption('lolwut', 'LOLWUT')];

  votingError: Boolean = false;
  votingErrorMessage: String;
  votingSuccess: Boolean = false;
  voted: Boolean = false;
  selectedFeature: PollOption;

  constructor(private http: HttpClient) {
  }

  canVote(): Boolean {
    return !this.voted;
  }

  onSelectionChange(selectedFeature: PollOption) {
    this.selectedFeature = selectedFeature;
  }

  onSubmit(): void {

    const vote: PollVote = new PollVote(this.selectedFeature.option);

    this.http.post(environment.baseUrl + 'polls/feature', vote, httpOptions).subscribe(ok => {
      this.voted = true;
      this.votingSuccess = true;
    }, err => {
      this.votingError = true;
      console.log(err);
      this.votingErrorMessage = err.toString();
    }, () => {
      this.voted = true;
      this.votingSuccess = true;
    });
  }
}

class PollVote {
  constructor(public feature: String) {
  }
}

class PollOption {
  constructor(public option: string, public label: string) {
  }
}
