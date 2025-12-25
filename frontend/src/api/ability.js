import axios from 'axios'

export const abilityAPI = {
  async getSummary() {
    const res = await axios.get('/api/ability/summary')
    return res.data
  },
  async getTimeseries() {
    const res = await axios.get('/api/ability/timeseries')
    return res.data
  }
}
