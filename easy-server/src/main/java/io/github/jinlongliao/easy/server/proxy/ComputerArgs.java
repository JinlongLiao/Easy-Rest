package io.github.jinlongliao.easy.server.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaojinlong
 * @since 2020/7/10 18:45
 */
public interface ComputerArgs {
    /**
     * @param source
     * @return
     */
    Object[] computer(Object[] source);


    class Default implements ComputerArgs {
        public final int computer;

        /**
         * 1.REQUEST
         * 2. RESPONSE
         *
         * @param computer
         */
        public Default(int computer) {
            this.computer = computer;
        }

        /**
         * @param source
         * @return
         */
        @Override
        public Object[] computer(Object[] source) {
            List objects = new ArrayList();
            switch (computer) {
                case 1:
                    objects.add(source[1]);
                    break;
                case 2:
                    objects.add(source[2]);
                    break;
                case 3:
                    objects.add(source[1]);
                    objects.add(source[2]);
                    break;
                case 0:
                default:
                    break;
            }
            return objects.toArray(new Object[0]);
        }
    }
}
