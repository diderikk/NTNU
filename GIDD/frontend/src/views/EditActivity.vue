<template>
  <div id="edit-activity">
    <div id="nav">
      <button @click="goBack" class="back-button">
        <i class="fa fa-arrow-left" aria-hidden="true"></i>
        Gå tilbake
      </button>
    </div>

    <h2>Endre Aktivitet</h2>
    <ImageSelector
      labelName=""
      @imageSelected="onSelectedImage"
      @removeImage="onRemoveImage"
    />
    <div>
      <h1>{{ activity.title }}</h1>
      <h3>Endre tittel på aktiviteten</h3>
      <input v-model="activity.title" type="title" placeholder="Tittel" />
      <div class="error-message-container">
        <p class="error-message" v-if="!isTitleValid">Oppgi gyldig tittel</p>
      </div>
    </div>
    <div>
      <h3>Endre startdato</h3>
      <div id="start-date-container">
        <select v-model="selectedYear" name="year">
          <option hidden disabled value>Velg år</option>
          <option
            v-for="(year, index) in availableYears"
            :value="year"
            :key="index"
          >
            {{ year }}
          </option>
        </select>
        <select v-model="selectedMonth" name="month">
          <option hidden disabled value>Velg måned</option>
          <option
            v-for="(month, index) in months"
            :value="month.name"
            :key="index"
          >
            {{ month.name }}
          </option>
        </select>
        <select v-model="selectedDay" name="day">
          <option hidden disabled value>Velg dato</option>
          <option
            v-for="index in daysInCurrentMonth"
            :value="index"
            :key="index"
          >
            {{ index }}
          </option>
        </select>
      </div>
    </div>

    <div>
      <h3>Endre start-tidspunkt</h3>
      <div id="start-time-container">
        <select v-model="selectedHour" name="hour">
          <option hidden disabled value>Velg time</option>
          <option v-for="index in hoursList" :value="index" :key="index">
            {{ index }}
          </option>
        </select>
        <select v-model="selectedMinute" name="minutes">
          <option hidden disabled value>Velg minutt</option>
          <option v-for="index in minutes" :value="index" :key="index">
            {{ index }}
          </option>
        </select>
      </div>
      <div class="error-message-container">
        <p v-if="!isDateTimeValid">Oppgi gyldig starttid og -dato</p>
      </div>
    </div>
    <div>
      <h3>Endre sted</h3>
      <div id="map-view">
        <Map
          id="map"
          :center="getCoordinates"
          :setLocation="true"
          :getLocation="true"
          :activityData="[]"
        ></Map>
      </div>
      <div id="place-container">
        <h5>Legg til et fysisk sted der aktiviteten skal ta plass</h5>
        <input v-model="activity.place" type="place" placeholder="Sted" />
        <input v-model="activity.city" type="city" placeholder="By" />
        <div class="error-message-container">
          <p class="error-message" v-if="!isPlaceValid">Oppgi et gyldig sted</p>
        </div>
      </div>
    </div>
    <div>
      <h3>Endre aktivitetstypen</h3>
      <input v-model="activity.type" type="type" placeholder="Type aktivitet" />
      <div class="error-message-container">
        <p class="error-message" v-if="!isTypeValid">
          Oppgi gyldig type aktivitet
        </p>
      </div>
    </div>
    <div>
      <h3>Endre maks antall deltagere</h3>
      <input
        v-model="activity.maxParticipants"
        type="maxNumberOfParticipants"
        placeholder="Maks antall deltagere"
      />
      <div class="error-message-container">
        <p class="error-message" v-if="!isNumberOfParticipantsValid">
          Oppgi gyldig maks antall deltagere
        </p>
      </div>
    </div>
    <div>
      <h3>Endre varigheten på aktiviteten (timer)</h3>
      <input v-model="durationDisplay" type="duration" placeholder="Varighet" />
      <div class="error-message-container">
        <p class="error-message" v-if="!isDurationValid">
          Oppgi gyldig varighet
        </p>
      </div>
    </div>

    <div>
      <h3>Endre beskrivelse</h3>
      <h5>Legg til en kort beskrivelse av aktiviteten (frivillig)</h5>
      <textarea
        id="description"
        v-model="activity.description"
        type="description"
        placeholder="Beskrivelse"
      />
    </div>
    <div>
      <h3>Endre utstyr</h3>
      <h5>
        Legg til utstyr som trengs for å gjennomføre aktiviteten (frivillig)
      </h5>
      <input
        v-model="activity.equipment"
        type="equipment"
        placeholder="Utstyr"
      />
    </div>
    <div>
      <h3>Endre belastningsnivå</h3>
      <h5>Hva slags belastningsnivå har aktiviteten?</h5>
      <div id="fitness-level-container">
        <div>
          <input v-model="isEasy" type="checkbox" id="easy" name="easy" />
          <label for="easy">Lav</label>
        </div>
        <div>
          <input v-model="isMedium" type="checkbox" id="medium" name="medium" />
          <label for="medium">Medium</label>
        </div>
        <div>
          <input v-model="isHard" type="checkbox" id="hard" name="hard" />
          <label for="hard">Høy</label>
        </div>
      </div>
      <div class="error-message-container">
        <p class="error-message" v-if="!isDifficultyValid">
          Oppgi gyldig vanskelighetsgrad
        </p>
      </div>
    </div>

    <p v-if="!isValidForm">
      Sjekk at du har fylt inn all nødvendig informasjon
    </p>
    <p v-if="showSuccessMessage">Endringene ble lagret!</p>
    <div id="edit-activity-buttons">
      <div>
        <button
          @click="saveActivityChanges"
          id="saveButton"
          :disabled="!isValidForm"
        >
          lagre
        </button>
      </div>
      <div>
        <button @click="cancelActivity" id="cancelButton">avlys</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  onBeforeMount,
  computed,
  Ref,
  reactive,
  provide,
  watch,
} from "vue";
import { useRouter } from "vue-router";
import axios from "@/axiosConfig";
import IEditActivity from "@/interfaces/EditActivity.interface";
import Month from "@/interfaces/Month.interface";
import ImageSelector from "@/components/ImageSelector.vue";
import { TrainingLevel } from "@/enums/TrainingLevel.enum";
import Map from "@/components/Map.vue";
import ICoordinates from "@/interfaces/ICoordinates.interface";
import data from "@/../config.json";
import axiosNotConfig from "axios";

export default defineComponent({
  name: "EditActivity",
  components: { ImageSelector, Map },
  props: { id: { required: true } },

  setup(props) {
    const router = useRouter();
    const apiKey = data.googleAPIKey;
    const activity: Ref<IEditActivity> = ref({
      title: "",
      activityPicture: "",
      city: "",
      description: "",
      difficulty: 0,
      durationMinutes: 0,
      equipment: "",
      latitude: 0,
      longitude: 0,
      maxParticipants: 0,
      place: "",
      privateActivity: false,
      startTime: "",
      type: "",
    } as IEditActivity);

    //Needed for the map
    const coordinates = reactive({ lat: 0.0, lng: 0.0 } as ICoordinates);
    provide("coordinates", coordinates);

    const selectedYear = ref("");
    const selectedMonth = ref("");
    const selectedDay = ref("");
    const currentYear = new Date().getFullYear();
    const limitForUpperYear = 10;
    const selectedHour = ref("");
    const selectedMinute = ref("");
    const isEasy = ref(false);
    const isMedium = ref(false);
    const isHard = ref(false);
    const showSuccessMessage = ref(false);

    //Change activit duration from minutes to hours
    const durationDisplay = ref(0);

    const onSelectedImage = (image: string) => {
      activity.value.activityPicture = image;
    };

    const onRemoveImage = () => {
      delete activity.value.activityPicture;
    };

    const daysInFebruary = computed(() => {
      return isLeapYear.value ? 29 : 28;
    });

    const months: Ref<Month[]> = ref([
      { name: "Januar", numberOfDays: 31 },
      { name: "Februar", numberOfDays: daysInFebruary },
      { name: "Mars", numberOfDays: 31 },
      { name: "April", numberOfDays: 30 },
      { name: "Mai", numberOfDays: 31 },
      { name: "Juni", numberOfDays: 30 },
      { name: "Juli", numberOfDays: 31 },
      { name: "August", numberOfDays: 31 },
      { name: "September", numberOfDays: 30 },
      { name: "Oktober", numberOfDays: 31 },
      { name: "November", numberOfDays: 30 },
      { name: "Desember", numberOfDays: 31 },
    ]);

    const hoursList = ref([
      "00",
      "01",
      "02",
      "03",
      "04",
      "05",
      "06",
      "07",
      "08",
      "09",
      "10",
      "11",
      "12",
      "13",
      "14",
      "15",
      "16",
      "17",
      "18",
      "19",
      "20",
      "21",
      "22",
      "23",
    ]);

    const minutes = ref([
      "00",
      "01",
      "02",
      "03",
      "04",
      "05",
      "06",
      "07",
      "08",
      "09",
    ]);

    const getCoordinates = computed(
      (): ICoordinates => {
        return {
          lat: activity.value.latitude,
          lng: activity.value.longitude,
        } as ICoordinates;
      }
    );

    const updateCityPlace = async () => {
      try {
        const response = await axiosNotConfig
          .get(
            `https://maps.googleapis.com/maps/api/geocode/json?latlng=${
              coordinates.lat + "," + coordinates.lng
            }&key=${apiKey}`
          )
          .then();
        const responseData = response.data;
        if (response.status == 200) {
          let address: string[] = (responseData.results[0]
            .formatted_address as string).split(",");
          let place = address[0];
          let city = address[1].split(" ");
          place != "Unnamed Road"
            ? (activity.value.place = place)
            : (activity.value.place = ""); //Setting the place value
          city = city.filter((element) => {
            //Filters away city names that is not valid
            if (element == "" || !isNaN(Number(element))) {
              return false;
            }
            return true;
          });
          city[0] || city[0] != "Unnamed"
            ? (activity.value.city = city[0])
            : (activity.value.city = ""); //Setting the city value
        }
      } catch (error) {
        //Something went wrong, user has to write place and city
      }
    };

    watch(
      () =>
        coordinates.lat ||
        coordinates.lng ||
        activity.value.latitude ||
        activity.value.longitude,
      (newValue, oldValue) => {
        if (newValue != oldValue) {
          updateCityPlace();
        }
      }
    );

    /**
     * Loads activity from database, has to set date, time, and difficulty.
     */
    onBeforeMount(async () => {
      try {
        const response = await axios.get(`/activities/${props.id}`);
        activity.value = response.data as IEditActivity;

        durationDisplay.value = activity.value.durationMinutes / 60;

        selectedYear.value = activity.value.startTime.substring(0, 4);
        if (activity.value.startTime.substring(5, 7) == "01") {
          selectedMonth.value = "Januar";
        } else if (activity.value.startTime.substring(5, 7) == "02") {
          selectedMonth.value = "Februar";
        } else if (activity.value.startTime.substring(5, 7) == "03") {
          selectedMonth.value = "Mars";
        } else if (activity.value.startTime.substring(5, 7) == "04") {
          selectedMonth.value = "April";
        } else if (activity.value.startTime.substring(5, 7) == "05") {
          selectedMonth.value = "Mai";
        } else if (activity.value.startTime.substring(5, 7) == "06") {
          selectedMonth.value = "Juni";
        } else if (activity.value.startTime.substring(5, 7) == "07") {
          selectedMonth.value = "Juli";
        } else if (activity.value.startTime.substring(5, 7) == "08") {
          selectedMonth.value = "August";
        } else if (activity.value.startTime.substring(5, 7) == "09") {
          selectedMonth.value = "September";
        } else if (activity.value.startTime.substring(5, 7) == "10") {
          selectedMonth.value = "Oktober";
        } else if (activity.value.startTime.substring(5, 7) == "11") {
          selectedMonth.value = "November";
        } else if (activity.value.startTime.substring(5, 7) == "12") {
          selectedMonth.value = "Desember";
        }

        if (activity.value.startTime.substring(8, 10) == "01") {
          selectedDay.value = "1";
        } else if (activity.value.startTime.substring(8, 10) == "02") {
          selectedDay.value = "2";
        } else if (activity.value.startTime.substring(8, 10) == "03") {
          selectedDay.value = "3";
        } else if (activity.value.startTime.substring(8, 10) == "04") {
          selectedDay.value = "4";
        } else if (activity.value.startTime.substring(8, 10) == "05") {
          selectedDay.value = "5";
        } else if (activity.value.startTime.substring(8, 10) == "06") {
          selectedDay.value = "6";
        } else if (activity.value.startTime.substring(8, 10) == "07") {
          selectedDay.value = "7";
        } else if (activity.value.startTime.substring(8, 10) == "08") {
          selectedDay.value = "8";
        } else if (activity.value.startTime.substring(8, 10) == "09") {
          selectedDay.value = "9";
        } else {
          selectedDay.value = activity.value.startTime.substring(8, 10);
        }

        selectedHour.value = activity.value.startTime.substring(11, 13);
        selectedMinute.value = activity.value.startTime.substring(14, 16);

        if (activity.value.difficulty === 1) {
          isEasy.value = true;
        } else if (activity.value.difficulty === 2) {
          isMedium.value = true;
        } else if (activity.value.difficulty === 3) {
          isEasy.value = true;
          isMedium.value = true;
        } else if (activity.value.difficulty === 4) {
          isHard.value = true;
        }

        if (minutes.value.length <= 10) {
          let n = "0";
          for (let i = 10; i < 60; i++) {
            n = String(i);
            // eslint-disable-next-line vue/no-side-effects-in-computed-properties
            minutes.value.push(n);
          }
        }
      } catch {
        router.push("/error");
      }
    });

    const goBack = (): void => {
      router.push("/activity-feed");
    };

    const isTitleValid = computed(() => {
      return activity.value.title.trim() !== "";
    });

    const isPlaceValid = computed(() => {
      return (
        activity.value.place.trim() !== "" && activity.value.city.trim() !== ""
      );
    });

    const isTypeValid = computed(() => {
      return activity.value.type.trim() !== "";
    });

    const isNumberOfParticipantsValid = computed(() => {
      return (
        !isNaN(activity.value.maxParticipants) &&
        activity.value.maxParticipants >= 2
      );
    });

    const isDurationValid = computed(() => {
      return activity.value.durationMinutes > 0;
    });

    const isDateTimeValid = computed(() => {
      return (
        selectedYear.value !== "" &&
        selectedMonth.value !== "" &&
        selectedDay.value !== "" &&
        selectedHour.value !== "" &&
        selectedMinute.value !== ""
      );
    });

    const isValidForm = computed(() => {
      return (
        isPlaceValid.value &&
        isTypeValid.value &&
        isNumberOfParticipantsValid.value &&
        isDateTimeValid.value
      );
    });

    const isDifficultyValid = computed(() => {
      return (
        activity.value.difficulty !== -1 &&
        (isEasy.value || isMedium.value || isHard.value)
      );
    });

    const calculateDifficulty = computed(() => {
      let difficultyNumber = 0;
      if (isEasy.value) {
        difficultyNumber += TrainingLevel.LOW;
      }
      if (isMedium.value) {
        difficultyNumber += TrainingLevel.MEDIUM;
      }
      if (isHard.value) {
        difficultyNumber += TrainingLevel.HIGH;
      }
      return difficultyNumber;
    });

    /**
     * Method to save activity changes. Checks if form is valid,
     * if so, it starts saving. If there is no activity picture,
     * this is set to string "null". Send response via axios,
     * awaits respons from update activity, and sets updated activity
     */
    const saveActivityChanges = async (): Promise<void> => {
      if (isValidForm.value) {
        try {
          if (!activity.value.activityPicture)
            activity.value.activityPicture = "null";

          activity.value.difficulty = calculateDifficulty.value;
          activity.value.startTime = makeDateTime.value;
          activity.value.latitude = coordinates.lat;
          activity.value.longitude = coordinates.lng;

          const response = await axios.put(
            `/activities/${props.id}`,
            activity.value
          );

          if (response.status === 200) {
            //window.alert("Endringene ble lagret");
            showSuccessMessage.value = true;
            //router.back();
          }
        } catch (error) {
          router.push("/error");
        }
      }
    };

    /**
     * Cancels/deletes an activity, makes a confirm window
     */
    const cancelActivity = async (): Promise<void> => {
      if (window.confirm("Er du sikker på at du vil avlyse aktiviteten din?")) {
        try {
          const response = await axios.delete(`/activities/${props.id}`);

          if (response.status === 200) {
            router.replace("/activity-feed");
          }
        } catch (error) {
          router.push("/error");
        }
      }
    };

    const availableYears = computed(() => {
      const years = [];
      for (let i = currentYear + limitForUpperYear; i > currentYear - 1; i--) {
        years.push(i);
      }
      return years.reverse();
    });

    /**
     * Method to return a boolean depending
     * on if the year selected is a leap year
     */
    const isLeapYear = computed(() => {
      if (parseInt(selectedYear.value) === 0) return false;
      return (
        parseInt(selectedYear.value) % 400 === 0 ||
        (parseInt(selectedYear.value) % 100 !== 0 &&
          parseInt(selectedYear.value) % 4 === 0)
      );
    });

    const daysInCurrentMonth = computed(() => {
      if (selectedMonth.value === "") {
        return 0;
      }
      return months.value.find(
        (month: { name: string }) => month.name === selectedMonth.value
      )?.numberOfDays;
    });

    const makeDateTime = computed(() => {
      return (
        selectedYear.value +
        "-" +
        getMonthNumber.value +
        "-" +
        getDay.value +
        " " +
        selectedHour.value +
        ":" +
        selectedMinute.value
      );
    });

    const getDay = computed(() => {
      if (parseInt(selectedDay.value) < 10) {
        return "0" + selectedDay.value;
      }
      return selectedDay.value.toString();
    });

    const getMonthNumber = computed(() => {
      if (selectedMonth.value === "Januar") {
        return "01";
      } else if (selectedMonth.value === "Februar") {
        return "02";
      } else if (selectedMonth.value === "Mars") {
        return "03";
      } else if (selectedMonth.value === "April") {
        return "04";
      } else if (selectedMonth.value === "Mai") {
        return "05";
      } else if (selectedMonth.value === "Juni") {
        return "06";
      } else if (selectedMonth.value === "Juli") {
        return "07";
      } else if (selectedMonth.value === "August") {
        return "08";
      } else if (selectedMonth.value === "September") {
        return "09";
      } else if (selectedMonth.value === "Oktober") {
        return "10";
      } else if (selectedMonth.value === "November") {
        return "11";
      } else if (selectedMonth.value === "Desember") {
        return "12";
      }
      return "Noe gikk galt";
    });

    return {
      activity,
      isValidForm,
      isDifficultyValid,
      isTitleValid,
      isDurationValid,
      calculateDifficulty,
      isPlaceValid,
      isTypeValid,
      isNumberOfParticipantsValid,
      isDateTimeValid,
      isEasy,
      isMedium,
      isHard,
      durationDisplay,

      getCoordinates,

      onSelectedImage,
      onRemoveImage,

      availableYears,
      daysInCurrentMonth,
      isLeapYear,
      selectedYear,
      selectedMonth,
      selectedDay,
      selectedHour,
      selectedMinute,
      makeDateTime,
      months,
      hoursList,
      minutes,

      goBack,
      showSuccessMessage,
      saveActivityChanges,
      cancelActivity,
    };
  },
});
</script>

<style scoped lang="scss">
$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#edit-activity {
  margin: 35px;
  text-align: left;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
    padding: 20px;
  }
}

h2 {
  text-align: center;
}

h3,
h5 {
  text-align: left;
}

h5 {
  font-weight: 500;
}

input {
  width: 100%;
}

img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin: 20px;
}

#host {
  display: grid;
  grid-template-rows: 1fr 0.5fr;
  justify-items: center;
}

#host.h4 {
  text-align: left;
}

select {
  width: 100%;
  height: 2rem;
  border-radius: 10px;
  border-width: 0;
  border-bottom: 2px RGBA(0 0 0/4%) solid;
  font-family: "Mulish", sans-serif;
  font-size: 1rem;
  text-align-last: center;
  color: #54545e;
}

#start-date-container {
  margin-bottom: 30px;
}

#start-date-container,
#start-time-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  column-gap: 30px;
}

#fitness-level-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  justify-items: center;
}

#fitness-level-container input {
  width: unset;
  margin: 10px;
}

#map {
  padding-top: 20px;
  position: absolute;
  width: 100%;
  left: 0px;
  height: 25%;
  @media only screen and (min-width: 600px) {
    height: 350px;
  }
}

#map-view {
  color: $primary-color;
  height: 270px;
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

#map {
  height: 250px;
}

#place-container {
  display: grid;
  grid-template-rows: 1fr 1fr;
  row-gap: 15px;
}

#description {
  height: 150px;
  width: 90%;
  align-content: flex-start;
  border: 1px solid #9f9f9f;
  border-radius: 20px;
  padding: $padding;
  font: normal 1rem "mulish", sans-serif;
  color: $primary-color !important;
}

.error-message-container {
  height: 30px;
}

.error-message {
  font-weight: 600;
  color: $secondary-color;
  font-size: 10px;
  line-height: 30px;
  margin: 0px;
}

#edit-activity-buttons {
  display: grid;
  width: 50%;
  grid-template-columns: 1fr 1fr;
  column-gap: 5px;
  margin: auto;
  justify-self: center;
  justify-items: center;
}

.back-button {
  color: $primary-color;
  background-color: unset;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  font-size: 0.7rem;
  width: 130px;
  text-align: left;
  padding: 10px 0px 10px 0px;
  display: block;
}

.back-button:hover {
  color: $secondary-color;
}
</style>
