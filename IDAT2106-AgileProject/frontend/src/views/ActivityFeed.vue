<template>
  <div>
    <div>
      <div id="activity-feed">
        <div>
          <div class="header" id="upper-header">
            <div id="header-title">
              <h2>Finn aktiviteter</h2>
            </div>
            <div id="view-container">
              <button id="view-list" class="icon"></button>
              <button
                @click="mapViewClicked"
                id="view-map"
                class="icon"
              ></button>
            </div>
          </div>

          <div class="header" id="lower-header">
            <div id="test">
              <div>
                <select
                  class="dropdown lower-header-item"
                  v-model="sortingType"
                  @change="sortClicked"
                >
                  <option selected hidden>Sortering</option>
                  <option value="DATE">Startdato</option>
                  <option value="DISTANCE">Avstand</option>
                  <option value="PARTICIPANT_AMOUNT">Antall deltakere</option>
                  <option value="REMOVE_SORT">Nullstill</option>
                </select>
              </div>

              <div>
                <input
                  type="text"
                  id="search"
                  v-model="searchQuery"
                  @change="sortClicked"
                  class="lower-header-item"
                  placeholder="Søk"
                />
              </div>
            </div>

            <div id="filter-boxes" class="lower-header-item">
              <div class="checkbox-label">
                <label for="easy">Lett</label>
                <input
                  type="checkbox"
                  v-model="easyCheckbox"
                  @change="sortClicked"
                />
              </div>
              <div class="checkbox-label">
                <label for="easy">Middels</label>
                <input
                  type="checkbox"
                  v-model="mediumCheckbox"
                  @change="sortClicked"
                />
              </div>
              <div class="checkbox-label">
                <label for="easy">Krevende</label>
                <input
                  type="checkbox"
                  v-model="hardCheckbox"
                  @change="sortClicked"
                />
              </div>
            </div>
          </div>
        </div>
        <div id="activities">
          <div v-if="isFeedEmpty">
            <p>Ingen tilgjengelige aktiviteter</p>
          </div>
          <div v-else-if="!isFeedEmpty">
          <ActivityFeedItem
            v-for="activity in activities"
            :key="activity.activityId"
            :activityData="activity"
          />
          </div>
        </div>
      </div>
    </div>
    <div id="add-activity" @click="makeActivity">+</div>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  onBeforeMount,
  reactive,
  ref,
  Ref,
} from "vue";
import ActivityFeedItem from "../components/ActivityFeedItem.vue";
import SortAndFilter from "../interfaces/SortAndFilter.interface";
import axios from "@/axiosConfig";
import { useRouter } from "vue-router";
import IActivity from "@/interfaces/Activity/IActivity.interface";

export default defineComponent({
  name: "ActivityFeed",
  components: {
    ActivityFeedItem,
  },

  setup() {
    const router = useRouter();
    const activities: Ref<IActivity[]> = ref([]);
    const easyCheckbox: Ref<boolean> = ref(false);
    const mediumCheckbox: Ref<boolean> = ref(false);
    const hardCheckbox: Ref<boolean> = ref(false);
    const amount: Ref<number | null> = ref(null);
    const sortingType: Ref<string> = ref("Sortering");
    const searchQuery: Ref<string> = ref("");
    const isFeedEmpty = ref(false);

    const getDifficulty = computed(() => {
      let difficulty = 0;
      if (easyCheckbox.value) difficulty += 1;
      if (mediumCheckbox.value) difficulty += 2;
      if (hardCheckbox.value) difficulty += 4;

      if (difficulty > 0) return difficulty;
      return null;
    });

    const getAmount = computed(() => {
      if (amount.value === null || amount.value < 0) return 1000;
      return amount.value;
    });

    const filter = computed(() => {
      return {
        sortingType: sortingType.value,
        searchQuery: searchQuery.value,
        difficulty: getDifficulty.value,
        amount: getAmount.value,
      } as SortAndFilter;
    });

    onBeforeMount(async () => {
      try {
        const response = await axios.get("/activities");
        activities.value = response.data as IActivity[];
        if (activities.value.length === 0 || activities.value === []) {
          isFeedEmpty.value = true;
        } else {
          isFeedEmpty.value = false;
        }
      } catch (error) {
        router.push("/error");
      }
    });

    /**
     * Runs when any inputs has been changed
     * Sends the filter to backend server
     */
    const sortClicked = async (): Promise<void> => {
      if (sortingType.value === "DISTANCE") {
        // Finds latitude and longtidue to user
        navigator.geolocation.getCurrentPosition(
          (posistion) => {
            filter.value.userLongitude = posistion.coords.longitude;
            filter.value.userLatitude = posistion.coords.latitude;
            getFilteredAndSortedActivities();
          },
          (err) => {
            delete filter.value.userLongitude;
            delete filter.value.userLatitude;
            sortingType.value = "Sortering";
          }
        );
      }else if(sortingType.value === "Sortering"){
        sortingType.value = "NONE";
        getFilteredAndSortedActivities();
        sortingType.value = "NONE";
      } 
      else {
        getFilteredAndSortedActivities();
      }
    };
    /**
     * Sends filter to backend, and sets activities list to reponse data
     */
    const getFilteredAndSortedActivities = async () => {
      try {
        const response = await axios.post(
          "/activities/alternatives",
          filter.value
        );
        activities.value = response.data as IActivity[];
        if (activities.value.length === 0 || activities.value === []) {
          isFeedEmpty.value = true;
        } else {
          isFeedEmpty.value = false;
        }
      } catch (error) {
        router.push("/error");
      }
    };

    const mapViewClicked = (): void => {
      router.push("/activity-map");
    };

    const makeActivity = (): void => {
      router.push("/make-activity");
    };

    return {
      sortClicked,
      easyCheckbox,
      mediumCheckbox,
      hardCheckbox,
      amount,
      sortingType,
      searchQuery,
      mapViewClicked,
      activities,
      makeActivity,
      isFeedEmpty,
    };
  },
});
</script>

<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Mulish&display=swap");

$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#activity-feed {
  color: $primary-color;
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

h2 {
  text-align: center;
}

.header {
  margin-bottom: 15px;
  text-align: left;
}

#upper-header {
  display: grid;
  grid-template-columns: 2fr 1fr;
}

#lower-header {
  display: grid;
  grid-template-columns: 1fr 3fr;
  row-gap: 20px;
}

h2 {
  font-weight: 600;
  text-align: left;
  margin: 0;
}

#view-container {
  text-align: right;
  align-content: center;
  display: block;
}

#view-list {
  background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23333333' viewBox='0 0 24 24'%3E%3Cpath fill-rule='evenodd' d='M9 5h11a1 1 0 010 2H9a1 1 0 110-2zm0 6h11a1 1 0 010 2H9a1 1 0 010-2zm0 6h11a1 1 0 010 2H9a1 1 0 010-2zm-4.5-6.5a1.5 1.5 0 110 3 1.5 1.5 0 010-3zm0 6a1.5 1.5 0 110 3 1.5 1.5 0 010-3zm0-12a1.5 1.5 0 110 3 1.5 1.5 0 010-3z'/%3E%3C/svg%3E");
}

#view-map {
  background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23c2c2c2' viewBox='0 0 24 24'%3E%3Cpath fill-rule='evenodd' d='M12 21c-1.5 0-7-6-7-11a7 7 0 1114 0c0 5-5.5 11-7 11zm0-8.5a2.5 2.5 0 100-5 2.5 2.5 0 000 5z'/%3E%3C/svg%3E");
}

.icon {
  margin-top: 7px;
  margin-left: 5px;
  align-items: flex-end;
  width: 22px;
  height: 22px;
  padding: 0px;
  background-color: unset;
}

.checkbox-label {
  display: flex;
  flex-direction: column;
  width: 10%;
  align-items: center;
}

#filter-boxes {
  display: grid;
  width: 100%;
  grid-template-columns: 33% 33% 33%;
  border-radius: 10px;
  border-bottom: 2px #e1e1e1 solid;
  justify-items: center;
  justify-self: center;
  padding: 5px 0 5px 0;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

#search {
  display: block;
}

#test {
  display: grid;
  grid-template-columns: 1fr 1fr;
  column-gap: 20px;
}

#lower-header {
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 1fr 1fr;
  column-gap: 10px;
  align-items: flex-end;
}

input {
  width: 100%;
}

.lower-header-item {
  border-radius: 20px;
  font-size: 10px;
  letter-spacing: 1px;
  border-color: #9f9f9f;
  color: #27282b;
  font-weight: 600 !important;
  padding: 5px 10px 5px 10px;
}

#lower-header-sort {
  box-shadow: 0px 0px 0px 1px #8b8b8b;
  border-width: 0;
  color: $primary-color;
  width: 150px;
}

#lower-header-filter {
  width: 50px;
}

option {
  line-height: 1rem;
}

#activity-button {
  position: fixed;
  bottom: 10px;
  left: 50%;
}

select {
  background: url(data:image/svg+xml;base64,PHN2ZyBpZD0iTGF5ZXJfMSIgZGF0YS1uYW1lPSJMYXllciAxIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0Ljk1IDEwIj48ZGVmcz48c3R5bGU+LmNscy0xe2ZpbGw6I2ZmZjt9LmNscy0ye2ZpbGw6IzQ0NDt9PC9zdHlsZT48L2RlZnM+PHRpdGxlPmFycm93czwvdGl0bGU+PHJlY3QgY2xhc3M9ImNscy0xIiB3aWR0aD0iNC45NSIgaGVpZ2h0PSIxMCIvPjxwb2x5Z29uIGNsYXNzPSJjbHMtMiIgcG9pbnRzPSIxLjQxIDQuNjcgMi40OCAzLjE4IDMuNTQgNC42NyAxLjQxIDQuNjciLz48cG9seWdvbiBjbGFzcz0iY2xzLTIiIHBvaW50cz0iMy41NCA1LjMzIDIuNDggNi44MiAxLjQxIDUuMzMgMy41NCA1LjMzIi8+PC9zdmc+)
    no-repeat 93% 50%;
  font-family: "Mulish", sans-serif;
  font-weight: 600;
  width: 100%;
  letter-spacing: 1px;
  -moz-appearance: none;
  -webkit-appearance: none;
  -webkit-border-radius: 0px;
  appearance: none;
  outline-width: 0;
  border-color: #bbbbbb;
}

#activities {
  margin-top: 35px;
  display: flex;
  flex-flow: wrap;
  position: relative;
}

#activity {
  padding: 10px 0px 10px 0px;
  @media only screen and (min-width: 800px) {
    padding: 10px;
  }
}

li {
  list-style-type: none;
  padding-bottom: 20px;
  @media only screen and (min-width: 800px) {
    width: 50%;
  }
}

#add-activity {
  line-height: 38px;
  border-radius: 40px;
  position: fixed;
  font-size: 1.3rem;
  bottom: 20px;
  height: 40px;
  width: 40px;
  left: 80%;
  color: #ffffff;
  background-color: $secondary-color;
  @media only screen and (min-width: 600px) {
    left: 76%;
    bottom: 40px;
  }
}

#add-activity:hover {
  cursor: pointer;
  background-color: #ff6666;
}
</style>
