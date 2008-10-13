#
# $Id: bindings.py 61 2003-11-28 21:34:59Z mdb $
# 
# lookuplet - a utility for quickly looking up information
# Copyright (C) 2001 Michael Bayne
# 
# This program is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation; either version 2.1 of the License, or (at your
# option) any later version.
# 
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
# 
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

require 'gconf2'

#
# Contains the configuration for a particular key binding and its associated
# query mapping."
#
class LBinding

  # constants defining which type of binding this is
  URL = 0
  EXEC = 1

  # our attributes
  attr_reader :key, :type, :name, :argument

  #
  # Constructs a binding with its requisite parameters.
  #
  def initialize (key, type, name, argument)
    @key = key
    @type = type
    @name = name
    @argument = argument
  end

  def invoke (terms)
    command = re.sub("%T", terms, @argument)
    command = re.sub("%U", urllib.quote(terms), command)
    if (@type == EXEC)
      posix.system(command)
    else
      # gnome.url_show(command)
      posix.system("gnome-mozilla '%s'" % command)
    end
  end

  def update (key, type, name, argument)
    modified = 0
    if (cmp(key, @key))
      @key = key
      modified = 1
    end

    if (type != @type)
      @type = type
      modified = 1
    end

    if (cmp(name, @name))
      @name = name
      modified = 1
    end

    if (cmp(argument, @argument))
      @argument = argument
      modified = 1
    end

    return modified
  end

  def to_string ()
    return sprintf("[key=%s, type=%d, name=%s, arg=%s]", @key, @type, @name, @argument)
  end
end

#
# Maintains the set of bindings loading into the program.
#
class LBindingSet

  # our gconf client instance
  @gclient

  # the list of LBinding objects
  @bindings

  #
  # The default constructor. Loads up our bindings from GConf.
  #
  def initialize ()
    # connect to the gconf server
    @gclient = GConf::Client.default
    @gclient.add_dir("/apps/lookuplet")

    @bindings = []

    count = 0

    #         gnome.config.push_prefix("/lookuplet/")
    #         count = gnome.config.get_int("lookuplet/bindings/count")
    #         for b in range(0, count):
    #             key = gnome.config.get_string("lookuplet/bindings/key_%.2u" % b)
    #             type = gnome.config.get_int("lookuplet/bindings/type_%.2u" % b)
    #             name = gnome.config.get_string("lookuplet/bindings/name_%.2u" % b)
    #             arg = gnome.config.get_string("lookuplet/bindings/arg_%.2u" % b)
    #             @bindings.append(LBinding(key, type, name, arg))
    #         gnome.config.pop_prefix()

    # if we loaded no bindings, use the defaults 
    if (count == 0)
      @bindings << LBinding.new("Control-g", LBinding::URL, "Google search",
                                "http://www.google.com/search?client=googlet&q=%U")
      @bindings << LBinding.new("Control-d", LBinding::EXEC, "Dictionary lookup",
                                "gdict -a '%T'")
      @bindings << LBinding.new("Control-Shift-d", LBinding::URL, "Debian package search",
                                "http://cgi.debian.org/cgi-bin/search_contents.pl?word=%U" +
                                "&case=insensitive&version=unstable&arch=i386" +
                                "&directories=yes")
      @bindings << LBinding.new("Control-f", LBinding::URL, "Freshmeat search",
                                "http://freshmeat.net/search/?q=%U")
      @bindings << LBinding.new("Control-i", LBinding::URL, "IMDB Title search",
                                "http://www.imdb.com/Tsearch?title=%U&restrict=Movies+only")

      # temporary hacked defaults
      @bindings << LBinding.new("Control-j", LBinding::EXEC, "Java doc lookup",
                                "/home/mdb/bin/quickdoc.pl \"%T\"")
    end
  end

  #
  # Returns the binding with a keysym matching the specified keysym or None if
  # no binding matches.
  #
  def get_match (keysym)
    return @bindings.find { |b| b.key == keysym }
  end

  #
  # Stores our updated bindings to our GConf preferences.
  #
  def flush ()
#         gnome.config.push_prefix("/lookuplet/")
#         gnome.config.set_int("lookuplet/bindings/count", len(@bindings))
#         b = 0
#         for binding in @bindings:
#             gnome.config.set_string(
#                 "lookuplet/bindings/key_%.2u" % b, binding.key)
#             gnome.config.set_int(
#                 "lookuplet/bindings/type_%.2u" % b, binding.type)
#             gnome.config.set_string(
#                 "lookuplet/bindings/name_%.2u" % b, binding.name)
#             gnome.config.set_string(
#                 "lookuplet/bindings/arg_%.2u" % b, binding.argument)
#             b += 1
#         gnome.config.sync()
#         gnome.config.drop_all()
#         gnome.config.pop_prefix()
  end
end
