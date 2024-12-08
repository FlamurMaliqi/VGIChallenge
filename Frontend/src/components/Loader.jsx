import React, {useContext, useRef} from 'react';
import {LoadingContext} from "../contexts/LoadingContext";

function Loader({msg, isError = false, isPopup = false }) {
    const playerRef = useRef(null);
    const { loadingMessage, setIsLoading, isLoadingError } = useContext(LoadingContext);

    const handleClose = () => {
        setIsLoading(false);
    };

    return (
        <div className={`horizontal-wrapper loader-wrapper ${isPopup ? 'popup' : ''}`}>
            <dotlottie-player
                ref={playerRef}
                src={`${process.env.PUBLIC_URL}/animations/bus-animation.json`}
                background="transparent"
                speed="1"
                style={{width: '100px', height: '100px'}}
                loop
                autoplay
            ></dotlottie-player>
            {msg ? (
                <p className={`message ${isError || isLoadingError ? 'error' : ''}`}>{msg}</p>
            ) : (
                <p className={`message ${isError || isLoadingError ? 'error' : ''}`}>{loadingMessage}</p>
            )}
            {isPopup && (
                <button className="close-button" onClick={handleClose}>
                    Schließen
                </button>
            )}
</div>
)
    ;
}

export default Loader;
