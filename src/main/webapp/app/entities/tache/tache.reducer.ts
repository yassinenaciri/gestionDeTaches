import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ITache, defaultValue } from 'app/shared/model/tache.model';
import { useAppSelector } from 'app/config/store';
import { Etat } from 'app/shared/model/enumerations/etat.model';
import { IStats } from 'app/shared/model/stats';

const initialState: EntityState<ITache> = {
  links: 'NonCommence',
  stats: {
    datasets: [
      {
        data: [1, 0, 0, 0, 0, 0],
        backgroundColor: ['#C0C0C0', '#275094', '#00FF00', '#FF7300', '#008000', '#db0f2b'],
      },
    ],
    labels: ['Non commencé', 'En cours', 'Terminé', 'Abondonné', 'Validé', 'Refusé'],
  },
  loading: false,
  errorMessage: null,
  entities: [],
  allEntities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/taches';
// Actions

export const getEntities = createAsyncThunk('tache/fetch_entity_list', async ({ page, size, sort, query }: IQueryParams, filter?) => {
  const requestUrl = `${apiUrl}${
    sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'
  }filter=${query}&cacheBuster=${new Date().getTime()}`;
  return axios.get<ITache[]>(requestUrl);
});

// retourne une liste au lieu d'une page avec etat== filtre
export const getALlEntities = createAsyncThunk('tache/fetch_allEntity_list', async (filter: string) => {
  const requestUrl = `${apiUrl}/getList?filter=${filter}`;
  return axios.get<ITache[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'tache/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITache>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);
export const getStats = createAsyncThunk(
  'tache/fetch_stats',
  async () => {
    const requestUrl = `${apiUrl}/stats`;
    return axios.get<IStats>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);
export const getStatsForEmploye = createAsyncThunk(
  'tache/fetch_stats_employe',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/stats/employe/${id}`;

    return axios.get<IStats>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const filtrer = createAsyncThunk(
  'tache/filtrer',
  (filtre: string) => {
    return filtre;
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'tache/create_entity',
  async (entity: ITache, thunkAPI) => {
    const result = await axios.post<ITache>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'tache/update_entity',
  async (entity: ITache, thunkAPI) => {
    const result = await axios.put<ITache>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'tache/partial_update_entity',
  async (entity: ITache, thunkAPI) => {
    const result = await axios.patch<ITache>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'tache/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ITache>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEtat = createAsyncThunk(
  'tache/updateEtat',
  async (object: { id: string | number; nouveauEtat: string }, thunkAPI) => {
    const requestUrl = `${apiUrl}/updateEtat/${object.id}`;
    const result = await axios.post<ITache>(requestUrl, object.nouveauEtat);
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const TacheSlice = createEntitySlice({
  name: 'tache',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(filtrer.fulfilled, (state, action) => {
        state.loading = false;
        state.links = action.payload;
      })
      .addCase(getALlEntities.fulfilled, (state, action) => {
        state.loading = false;
        state.allEntities = action.payload.data;
      })
      .addCase(getStats.fulfilled, (state, action) => {
        state.loading = false;
        // eslint-disable-next-line no-console
        console.log(action.payload.data.nonCommence);
        state.stats = {
          ...state.stats,
          datasets: [
            {
              data: [
                action.payload.data.nonCommence,
                action.payload.data.encours,
                action.payload.data.termine,
                action.payload.data.abondonne,
                action.payload.data.valide,
                action.payload.data.refuse,
              ],
              backgroundColor: ['#C0C0C0', '#275094', '#00FF00', '#FF7300', '#008000', '#db0f2b'],
            },
          ],
        };
        // state.stats.datasets[0].data = action.payload.data;
      })
      .addCase(getStatsForEmploye.fulfilled, (state, action) => {
        state.loading = false;
        // eslint-disable-next-line no-console
        console.log(action.payload.data.nonCommence);
        state.stats = {
          ...state.stats,
          datasets: [
            {
              data: [
                action.payload.data.nonCommence,
                action.payload.data.encours,
                action.payload.data.termine,
                action.payload.data.abondonne,
                action.payload.data.valide,
                action.payload.data.refuse,
              ],
              backgroundColor: ['#C0C0C0', '#275094', '#00FF00', '#FF7300', '#008000', '#db0f2b'],
            },
          ],
        };
        // state.stats.datasets[0].data = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity, updateEtat), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity, getALlEntities), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity, updateEtat), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = TacheSlice.actions;

// Reducer
export default TacheSlice.reducer;
