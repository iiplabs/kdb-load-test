const axios = require('axios')

let runner = async _ => {
  // average value of array
  const avg = arr => arr.reduce((acc,v,i,a)=>(acc+v/a.length),0)
  // random number between MIN / MAX
  const random = (min, max) => Math.floor(Math.random() * (max - min)) + min

  const getMetaByMsisdn = msisdn => {
    const start = Date.now()
    return axios.get(`http://localhost:9091/api/v1/find-meta-by-msisdn?msisdn=${msisdn}`).then((response) => {
      const { data: meta } = response
      const finish = Date.now()
      const time = finish - start
      return { meta, time }
    }).catch(_ => { 
      return {
        meta: [], 
        time: -1
      }
    })
  }

  console.log(`${new Date()} - Requested all MSISDN numbers`)
  const { data: allMsisdn } = await axios.get(
    `http://localhost:9091/api/v1/get-all-msisdn`
  )
  console.log(`${new Date()} - Received ${allMsisdn.length} numbers`)

  /* console.log(`${new Date()} - Measuring database access time`)
  let responseTimes = []
  for (const msisdn of allMsisdn) {
    const responseTime = await getMetaByMsisdn(msisdn)
    responseTimes.push(responseTime)
  }
  let errorsCount = [...responseTimes].filter(v => v.time === -1).length
  console.log(`${new Date()} - Error responses count: ${errorsCount}`)
  let averageResponseTime = avg([...responseTimes].filter(v => v.time > -1).map(v => v.time))
  console.log(`${new Date()} - Average response time: ${averageResponseTime} ms`)

  console.log(`${new Date()} - Measuring cached access time`)
  responseTimes = []
  for (const msisdn of allMsisdn) {
    const responseTime = await getMetaByMsisdn(msisdn)
    responseTimes.push(responseTime)
  }
  errorsCount = [...responseTimes].filter(v => v.time === -1).length
  console.log(`${new Date()} - Error responses count: ${errorsCount}`)
  averageResponseTime = avg([...responseTimes].filter(v => v.time > -1).map(v => v.time))
  console.log(`${new Date()} - Average response time: ${averageResponseTime} ms`)

  console.log(`${new Date()} - Requested all MSISDN numbers again to reset the cache`)
  await axios.get(
    `http://localhost:9091/api/v1/get-all-msisdn`
  ) */

  console.log(`${new Date()} - Measuring random database access time`)
  const responseTimes = []
  // for (const msisdn of allMsisdn) {
  for (let i = 0; i < 500000; i++) {
    const randomMsisdn = allMsisdn[random(0, allMsisdn.length - 1)]
    const responseDataAndTime = await getMetaByMsisdn(randomMsisdn)
    responseTimes.push(responseDataAndTime)
  }

  const errorsCount = [...responseTimes].filter(v => v.time === -1).length
  console.log(`${new Date()} - Error responses count: ${errorsCount}`)
  const averageResponseTime = avg([...responseTimes].filter(v => v.time > -1).map(v => v.time))
  console.log(`${new Date()} - Average response time: ${averageResponseTime} ms`)

  /* console.log(`${new Date()} - Requested all MSISDN numbers again to reset the cache`)
  await axios.get(
    `http://localhost:9091/api/v1/get-all-msisdn`
  ) */
}

runner()
