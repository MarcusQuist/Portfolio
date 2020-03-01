import React from 'react';
import styles from './Genre.module.css';

class Genre extends React.Component{
    constructor(props){
        super(props);
        this.addGenre = this.addGenre.bind(this);
        this.removeGenre = this.removeGenre.bind(this);
    }

    renderAction(){
        if(this.props.isRemoval){
            return <button className={styles.GenreAction}
                            onClick={this.removeGenre}>-</button>;
        }
        else{
            return <button className={styles.GenreAction}
                           onClick={this.addGenre}>+</button>;
        }
    }

    addGenre(){
        this.props.onAdd(this.props.genre);
    }

    removeGenre(){
        this.props.onRemove(this.props.genre);
    }


    render(){
        return(
            <div className={styles.Genre}>
                <div className={styles.GenreInformation}>
                    <img src='https://www.kindpng.com/picc/m/464-4645792_music-tune-ringtone-song-audio-melody-music-and.png'
                          className={styles.GenreImg}  
                            ></img>
                    <h3>{this.props.genreName}</h3>
                </div>
                {this.renderAction()}
            </div>
        )
    }
}

export default Genre;

