import MakeActivity from "./MakeActivity.interface";

export default interface IActivity extends MakeActivity {
  activityId: number;
  organizerId: number;
  orgarganizerForename?: string;
  organizerSurname?: string;
}
