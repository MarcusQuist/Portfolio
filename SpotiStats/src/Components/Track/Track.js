import React from 'react';
import styles from './Track.module.css';

class Track extends React.Component {
    constructor(props){
        super(props);
        this.addTrack = this.addTrack.bind(this);
        this.removeTrack = this.removeTrack.bind(this);
    }

    renderAction(){
        if(this.props.isRemoval){
            return <button className={styles.TrackAction}
                            onClick={this.removeTrack}>-</button>;
        }
        else{
            return <button className={styles.TrackAction}
                           onClick={this.addTrack}>+</button>;
        }
    }

    addTrack(){
        this.props.onAdd(this.props.track);
    }

    removeTrack(){
        this.props.onRemove(this.props.track);
    }

    render() {
      return (
        <div className={styles.Track}> 
            <div className={styles.TrackInformation}>
                <h3>{this.props.track.name}</h3>
                <p>{this.props.track.artist} | {this.props.track.album}</p>
            </div>
            {this.renderAction()}
        </div>
        );
    }
  }
export default Track;