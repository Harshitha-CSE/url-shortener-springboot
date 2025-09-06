import React, { useEffect } from 'react'
import { FaExternalLinkAlt, FaRegCalendarAlt } from 'react-icons/fa'
import { MdAnalytics, MdOutlineAdsClick } from 'react-icons/md'
import dayjs from 'dayjs'
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { IoCopy } from 'react-icons/io5'
import { LiaCheckSolid } from 'react-icons/lia'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { useStoreContext } from '../../contextApi/ContextApi';
import api from '../../api/api';
import { RotatingLines } from 'react-loader-spinner';
import Graph from './Graph';

const ShortenItem = ({originalUrl, shortUrl, clickCount, createdDate, expiryDate, fullShortUrl}) => {

    const {token} = useStoreContext();
    const navigate = useNavigate();
    const [isCopied, setIsCopied] = useState(false);
    const [analyticToggle, setAnalyticToggle] = useState(false);
    const [selectedUrl, setSelectedUrl] = useState("");
    const [loader, setLoader] = useState(false);
    const [analyticsData, setAnalyticsData] = useState([]);

    // REDIRECT FEATURE - use full short URL from backend or fallback to frontend domain
    const displayUrl = fullShortUrl || `${import.meta.env.VITE_REACT_SUBDOMAIN}/${shortUrl}`;
    const subDomain = fullShortUrl ? fullShortUrl.replace(/^https?:\/\//, "") : import.meta.env.VITE_REACT_SUBDOMAIN.replace(/^https?:\/\//, "");

    const analyticsHandler = (shorturl) => {
        if(!analyticToggle) {
            setSelectedUrl(shorturl);
        }
        setAnalyticToggle(!analyticToggle);
    };

    const fetchMyShortUrl = async()=>{
        setLoader(true);
        try{
            const {data} = await api.get(`/api/urls/analytics/${selectedUrl}?StartDate=2024-12-01T00:00:00&EndDate=2025-12-07T23:59:59`,{
                        headers: {
                            "Content-Type": "application/json",
                            Accept: "application/json",
                            Authorization: "Bearer " + token,
                        },
                    });
                    setAnalyticsData(data);
                    setSelectedUrl("");
                    console.log(data);

        } catch(error){
            navigate("/error");
            console.log(error);

        } finally{
            setLoader(false);
        }
    }
    useEffect(() => {
        if(selectedUrl) {
            fetchMyShortUrl();
        }
    },[selectedUrl]);
  return (
    <div className='bg-slate-100 shadow-lg border border-dotted border-slate-500 px-6 sm:py-1 py-3 rounded-md'>
        <div className={'flex sm:flex-row flex-col sm:justify-between w-full sm:gap-0 gap-5 py-5'}>
            <div className='flex-1 sm:space-y-1 max-w-full overflow-x-auto overflow-y-hidden'>
                <div className='text-slate-900 pb-1 sm:pb-0 flex items-center gap-2'>
                    {/* REDIRECT FEATURE - use full short URL for clicking */}
                    <a href={displayUrl} 
                    target='_blank'
                    className='text-[17px] font-montserrat font-[600] text-linkColor'>
                        {subDomain}
                    </a>
                    <FaExternalLinkAlt className='text-linkColor' />
                </div>
                <div className='flex items-center gap-1'>
                    <h3 className='text-gray-600 font-[400] text-[17px]'>
                        {originalUrl}
                    </h3>
                </div>
                <div className='flex items-center gap-8 pt-6'>
                    <div className='flex gap-1 items-center font-semibold text-green-800'>
                        <span>
                            <MdOutlineAdsClick className='text-[22px] me-1' />
                        </span>
                        <span className='text-[16px]'>{clickCount}</span>
                        <span className='text-[15px]'>
                            {clickCount === 0 || clickCount === 1 ? "Click" : "Clicks"}
                            </span>
                    </div>
                    <div className='flex items-center gap-2 font-semibold text-lg text-slate-800'>
                        <span>
                            <FaRegCalendarAlt/>
                        </span>
                        <span className='text-[17px]'>{dayjs(createdDate).format("MMM DD, YYYY")}</span>
                    </div>
                    {/* NEW FEATURE: expiry date - display expiry date if available */}
                    {expiryDate && (
                        <div className='flex items-center gap-2 font-semibold text-lg text-orange-600'>
                            <span>
                                <FaRegCalendarAlt/>
                            </span>
                            <span className='text-[17px]'>Expires: {dayjs(expiryDate).format("MMM DD, YYYY")}</span>
                        </div>
                    )}
                </div>
            </div>
            <div className='flex flex-1 sm:justify-end items-center ga-4'>
                {/* REDIRECT FEATURE - copy full short URL */}
                <CopyToClipboard
                onCopy={() => setIsCopied(true)}
                text={displayUrl}
                >
            <div className='flex cursor-pointer gap-1 items-center bg-btnColor py-2 font-semibold shadow-md shadow-slate-500 px-6 rounded-md text-white'>
                <button className=''>{isCopied ? "Copied!" : "Copy"}</button>
                {isCopied ? (
                    <LiaCheckSolid className='text-md'/>
                ):(
                    <IoCopy className='text-md'/>
                )}
            </div>
            </CopyToClipboard>
            <div
            onClick={() => analyticsHandler(shortUrl)}
            className='flex cursor-pointer gap-1 items-center bg-rose-700 py-2 font-semibold shadow-md shadow-slate-500 px-6 rounded-md text-white'>
                <button>Analytics</button>
                <MdAnalytics className='text-md'/>
            </div>
            </div>
        </div>
        <React.Fragment>
            <div className={`${
                analyticToggle ? "flex" : "hidden"
                } max-h-96 sm:mt-0 mt-5 min-h-96 relative border-t-2 w-[100%] overflow-hidden`}>
                    {loader ? (
                        <div className='min-h-[calc(450px-140px)] flex justify-center items-center w-full'>
                            <div className='flex flex-col items-center gap-1'>
                                <RotatingLines
                                    visible={true}
                                    height="50"
                                    width="50"
                                    color="#306cce"
                                    strokeWidth="5"
                                    animationDuration="0.75"
                                    ariaLabel="rotating-lines-loading"
                                    wrapperStyle={{}}
                                    wrapperClass=""
                                    />
                                    <p className='text-slate-700'>Please wait...</p>
                                </div>
                        </div>
                    ):(
                        <>{analyticsData.length === 0 &&(
                        <div className='absolute flex flex-col justify-center sm:items-center items-end top-0 left-0 w-full h-full bg-gray-100/50 z-10'>
                            <h1 className='text-slate-800 font-serif sm:text-2xl text-[18px] font-bold mb-1'>
                                No Data Available for This Time Period
                            </h1>
                            <h3 className='sm:w-96 w-[90%] sm:ml-0 pr-6 text-center sm:text-lg text-sm text-gray-600 mt-2'>
                                Share your short link to view where your engagements are
                                comming from
                            </h3>
                        </div>
                        )}
                        <Graph graphData={analyticsData} />
                        </>
                    )}

            </div>
        </React.Fragment>
        </div>
  )
}

export default ShortenItem