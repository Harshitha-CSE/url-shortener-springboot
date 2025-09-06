
import React, { useState } from 'react';
import { FaLink } from 'react-icons/fa';

import Graph from './Graph';
import ShortenPopUp from './ShortenPopUp';

import { useStoreContext } from '../../contextApi/ContextApi';
import { useFetchMyShortUrls, useFetchTotalClicks } from '../../hooks/useQuery';
import ShortenUrlList from './ShortenUrlList';
import { useNavigate } from 'react-router-dom';
import Loader from '../Loader';


const DashboardLayout = () => {
  const navigate = useNavigate();
  //const refetch = false;
  const {token} = useStoreContext();
  const [shortenPopUp, setShortenPopUp] = useState(false);
  //console.log(useFetchTotalClicks(token, onError));

  const {isLoading, data: myShortenUrls,refetch} = useFetchMyShortUrls(token, onError);
  const {isLoading: loader, data: totalClicks} = useFetchTotalClicks(token, onError);
  function onError() {
    navigate("/error");
  }
  return (
    <div className="lg:px-14 sm:px-8 px-4 min-h-[calc(100vh-64px)]">
      {loader ? (
        <Loader />
      ):(
        <div className='lg:w-[90%] w-full mx-auto py-16'>
            <div className='h-96 relative'>
              {totalClicks.length === 0 && (
                <div className='absolute flex flex-col justify-center sm:items-center items-start top-0 left-0 w-full h-full bg-gray-100/50 z-10'>
                  <h1 className='text-slate-800 font-serif sm:text-2xl text-[18px] font-bold'>
                    No Data For This time period
                    </h1>
                    <h3 className='sm:w-96 w-[90%] sm:ml-0 pl-6 text-center sm:text-lg text-sm text-gray-600 mt-2'>
                      Share your short link to view where your engagements are 
                      coming from
                      </h3>
                </div>
              )}
                <Graph graphData={totalClicks} />
            </div>
            <div className='py-5 sm:text-end text-center'>
              <button
              className='bg-custom-gradient px-4 py-2 rounded-md text-white font-semibold hover:brightness-90 transition-all'
              onClick={() => setShortenPopUp(true)}
              >
                Create a New Short URL
              </button>
            </div>
            <div>
              {!isLoading && myShortenUrls.length === 0 ? (
                <div className='flex justify-center pt-16'>
                  <div className='flex gap-2 items-center justify-center py-6 sm:px-8 px-5 rounded-md shadow-lg bg-gray-50'>
                    <h1 className='text-slate-800 font-montserrat sm:text-[18px] text-[14px] font-semibold mb-1'>
                      You have not created any short URLs yet.
                    </h1>
                    <FaLink className='text-blue-500 sm:text-xl text-sm' />
                  </div>
                </div>
              ) : (
                <ShortenUrlList data={myShortenUrls} />

              )}
            </div>
            
        </div>
      )}
      <ShortenPopUp
      refetch={refetch}
      open={shortenPopUp}
      setOpen={setShortenPopUp}
    />
    </div>
  )
}

export default DashboardLayout