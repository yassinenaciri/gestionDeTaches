import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IEmploye, defaultValue } from 'app/shared/model/employe.model';
import { ITache } from 'app/shared/model/tache.model';
import { activateAction } from 'app/modules/account/activate/activate.reducer';

const initialState: EntityState<IEmploye> = {
  loading: false,
  errorMessage: null,
  links: [],
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/employes';

// Actions

export const getEntities = createAsyncThunk('employe/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IEmploye[]>(requestUrl);
});

export const getEmployeOccupe = createAsyncThunk('employe/fetch_employe_occupe', async () => {
  const requestUrl = `${'api/taches'}?filter=${'Encours'}&cacheBuster=${new Date().getTime()}`;
  const listeTacheEncours: ITache[] = (await axios.get<ITache[]>(requestUrl)).data;
  const listeIdEmployeOccupe: string[] = [];
  listeTacheEncours.forEach(a => {
    listeIdEmployeOccupe.push(a.cadreAffecte.id.toString());
  });
  return listeIdEmployeOccupe;
});

export const getEntity = createAsyncThunk(
  'employe/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IEmploye>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'employe/create_entity',
  async (entity: IEmploye, thunkAPI) => {
    const result = await axios.post<IEmploye>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'employe/update_entity',
  async (entity: IEmploye, thunkAPI) => {
    const result = await axios.put<IEmploye>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'employe/partial_update_entity',
  async (entity: IEmploye, thunkAPI) => {
    const result = await axios.patch<IEmploye>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'employe/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IEmploye>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const EmployeSlice = createEntitySlice({
  name: 'employe',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addCase(getEmployeOccupe.fulfilled, (state, action) => {
        state.links = action.payload;
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = EmployeSlice.actions;

// Reducer
export default EmployeSlice.reducer;
